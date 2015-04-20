package controllers

import akka.LifeActors
import models.StartCommand._
import play.api.libs.EventSource
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.iteratee.{Concurrent, Enumeratee}
import play.api.libs.json.JsValue
import play.api.mvc._

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
