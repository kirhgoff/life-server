package controllers

import play.api.libs.json._
import play.api.mvc._
import models.Book._

import org.kirhgoff.ap.core.WorldGenerator
import org.kirhgoff.ap.core.WorldModel
import org.kirhgoff.ap.core.WorldPrinter
import org.kirhgoff.ap.core.LifeGenerator

object Application extends Controller {

  def generate = Action {
    Ok(Json.toJson(sampleWorld(10,10,'-','o')))
  }

  def saveBook = Action(BodyParsers.parse.json) { request =>
    val b = request.body.validate[Book]
    b.fold(
      errors => {
        BadRequest(Json.obj("status" -> "OK", "message" -> JsError.toFlatJson(errors)))
      },
      book => {
        addBook(book)
        Ok(Json.obj("status" -> "OK"))
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
