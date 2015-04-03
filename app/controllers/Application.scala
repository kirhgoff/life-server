package controllers

import play.api.libs.json._
import play.api.mvc._
import models.StartCommand._

import org.kirhgoff.ap.core.WorldGenerator
import org.kirhgoff.ap.core.WorldModel
import org.kirhgoff.ap.core.WorldPrinter
import org.kirhgoff.ap.core.LifeGenerator

object Application extends Controller {

  def generate = Action {request =>
    val command = request.body.validate[StartCommand]
    command.fold(
      errors => {
        BadRequest(Json.obj("status" -> "OK", "message" -> JsError.toFlatJson(errors)))
      },
      book => {
        Ok(Json.toJson(sampleWorld(command.width,command.height,'-','o')))
      }
    )
  }

  def index = Action {
    Ok(views.html.react.render("Hello world"));
  }

  def sampleWorld(width:Int, height:Int, dead:Char, alive:Char) = {
    val world = WorldGenerator.generate(width, height)
    LifeGenerator.applyLife(0.6, world)
    new WorldPrinter (dead, alive).print(world)
  }
}
