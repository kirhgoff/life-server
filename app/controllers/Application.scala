package controllers

import akka.LifeActors
import models.StartCommand._
import models.World
import org.kirhgoff.ap.core.{WorldPrinter, WorldModelListener, WorldModel}
import org.kirhgoff.ap.model.lifegame.{LifeGameWorldGenerator}
import play.api.libs.EventSource
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.iteratee.{Concurrent, Enumeratee}
import play.api.libs.json.{Json, JsValue}
import play.api.mvc._

object PlayWorldModelListener extends WorldModelListener {
  override def worldUpdated(world: WorldModel): Unit = {
    val worldPrinter:WorldPrinter = world.printer
    val json: JsValue = Json.toJson(World(worldPrinter.toAsciiSquare(world)))
    Application.lifeChannel.push(json)
  }
}

object Application extends Controller {
  val MaxSize = 50
  val MaxIterations = 500

  val DefaultSize = 10
  val DefaultIterations = 100

  val LifeRatio = 0.6

  val (lifeOut, lifeChannel) = Concurrent.broadcast[JsValue]
  
  def index = Action {
    Ok(views.html.react.render("Life Game"))
  }

  def start = Action (parse.json) {request =>
    val commandJsResult = request.body.validate[StartCommand]
    val command = commandJsResult.getOrElse(StartCommand(DefaultSize, DefaultSize, Some(DefaultIterations)))
    command match {
      case StartCommand(width, height, _) if width > MaxSize || height > MaxSize => {
        BadRequest(s"Cannot create world that big size, should be lesser than $MaxSize")
      }
      case StartCommand(_, _, Some(iterations)) if iterations > MaxIterations => {
        BadRequest(s"More than $MaxIterations are not allowed")
      }
      case StartCommand(width, height, iterationsOption) => {
        val world: WorldModel = new LifeGameWorldGenerator(LifeRatio).generate(width, height)
        println("Started with world:\n" + world.printer.toAsciiSquare(world))

        LifeActors.run(world, PlayWorldModelListener, iterationsOption match {
          case None => DefaultIterations
          case Some(iterations) => iterations
        })
        Ok("Life started")
      }
      case _ => BadRequest("Incorrect data, cannot parse")
    }
  }

  def stop = Action {
    LifeActors.stop
    Ok("Stopped")
  }

  def lifeFeed() = Action { request =>
    println(request.remoteAddress + " - client connected")
    Ok.feed(lifeOut
      &> connDeathWatch(request.remoteAddress)
      &> EventSource()
    ).as("text/event-stream") 
  }

  def connDeathWatch(addr: String): Enumeratee[JsValue, JsValue] = 
    Enumeratee.onIterateeDone{ () => println(addr + " - disconnected")
  }
}
