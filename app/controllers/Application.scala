package controllers

import play.api.libs.json._
import play.api.mvc._
import models.StartCommand._

import org.kirhgoff.ap.core.WorldGenerator
import org.kirhgoff.ap.core.WorldModel
import org.kirhgoff.ap.core.WorldPrinter
import org.kirhgoff.ap.core.LifeGenerator

object Application extends Controller {

  val (lifeOut, lifeChannel) = Concurrent.broadcast[JsValue]
  
  def index = Action {
    Ok(views.html.react.render("Hello world"));
  }

  def start = Action (parse.json) {request =>
    val commandJsResult = request.body.validate[StartCommand]
    val command = commandJsResult.getOrElse(StartCommand("10", "10"))
    val width = Integer.valueOf(command.width)
    val height = Integer.valueOf(command.height)
    LifeActors.run(width, height)

    Ok("Life started")
    //Ok(Json.toJson(sampleWorld(width,height,'-','o')))
  }

  def stop = Action {
    LifeActors.stop
  }

  def lifeFeed() = Action { request =>
    println(request.remoteAddress + " - client connected")
    Ok.feed(lifeOut
//      &> filter(room) 
//      &> Concurrent.buffer(50) 
      &> connDeathWatch(req.remoteAddress)
      &> EventSource()
    ).as("text/event-stream") 
  }

  def connDeathWatch(addr: String): Enumeratee[JsValue, JsValue] = 
    Enumeratee.onIterateeDone{ () => println(addr + " - disconnected") }

  // def sampleWorld(width:Int, height:Int, dead:Char, alive:Char) = {
  //   val world = WorldGenerator.generate(width, height)
  //   LifeGenerator.applyLife(0.6, world)
  //   new WorldPrinter (dead, alive).print(world)
  // }
}
