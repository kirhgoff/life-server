package controllers

import akka.LifeActors
import models.StartCommand._
import models.World
import org.kirhgoff.ap.core.{WorldModel, WorldModelListener, WorldPrinter}
import org.kirhgoff.ap.model.lifegame.LifeGameWorldGenerator
import play.api.{Logger, Play}
import play.api.libs.EventSource
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.iteratee.{Concurrent, Enumeratee}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._

object PlayWorldModelListener extends WorldModelListener {
  override def worldUpdated(world: WorldModel): Unit = {
    val worldPrinter:WorldPrinter = world.printer
    val json: JsValue = Json.toJson(World(worldPrinter.toAsciiSquare(world)))
    Application.lifeChannel.push(json)
  }
}

//TODO cut org.kirhgoff.ap out and put it to another module
object Application extends Controller {

  val logger = Logger(this.getClass)
  //Move to Seed
  val MaxSize = 50
  val MaxIterations = 500

  val DefaultSize = 10
  val DefaultIterations = 100

  val LifeRatio = 0.6

  val (lifeOut, lifeChannel) = Concurrent.broadcast[JsValue]
  
  def index = Action {request =>
    logger.info(s"Received index request: $request")
    Ok(views.html.react.render("Life Game"))
  }

  def start = Action (parse.json) {request =>
    val commandJsResult = request.body.validate[StartCommand]
    val command = commandJsResult.getOrElse(StartCommand(DefaultSize, DefaultSize, Some(DefaultIterations)))
    //TODO extract to check method
    command match {
      case StartCommand(width, height, _) if width > MaxSize || height > MaxSize => {
        logger.warn(s"Refusing to start, world is too big: [$width, $height]")
        BadRequest(s"Cannot create world that big size, should be lesser than $MaxSize")
      }
      case StartCommand(_, _, Some(iterations)) if iterations > MaxIterations => {
        logger.warn(s"Refusing to start, too many iterations: $iterations")
        BadRequest(s"More than $MaxIterations are not allowed")
      }
      case StartCommand(width, height, iterationsOption) => {
        val world: WorldModel = new LifeGameWorldGenerator(LifeRatio)
          .generate(width, height)
        logger.info("Starting with world:\n" + world.printer.toAsciiSquare(world))

        LifeActors.run(world, PlayWorldModelListener, iterationsOption match {
          case None => DefaultIterations
          case Some(iterations) => iterations
        })

        logger.info("Big Soup started")
        Ok("Life started")
      }
      case _ => {
        logger.warn(s"Incorrect request: $request")
        BadRequest("Incorrect data, cannot parse")
      }
    }
  }

  def stop = Action { implicit request =>
    logger.info("Stopped with " + request)
    LifeActors.stop
    Ok("Stopped")
  }

  def shutdown = Action { implicit request =>
    logger.info("Stopped with " + request)
    LifeActors.stop
    //Play.stop(Application.this)
    Ok("Shutdown")
  }


  def lifeFeed() = Action { request =>
    logger.info(request.remoteAddress + " - client connected")
    //WTF!?
    Ok.feed(lifeOut
      &> connDeathWatch(request.remoteAddress)
      &> EventSource()
    ).as("text/event-stream") 
  }

  def connDeathWatch(addr: String): Enumeratee[JsValue, JsValue] = 
    Enumeratee.onIterateeDone{ () => logger.info(addr + " - disconnected")
  }
}
