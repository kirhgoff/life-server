package controllers

import play.api.libs.json._
import play.api.mvc._
import models.Book._

import org.kirhgoff.ap.core.WorldGenerator
import org.kirhgoff.ap.core.WorldModel
import org.kirhgoff.ap.core.WorldPrinter
import org.kirhgoff.ap.core.LifeGenerator

object Application extends Controller {

  def listBooks = Action {
    Ok(Json.toJson(books))
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
    Ok(views.html.index.render("Hello world"));
  }

  def check = Action {
    val world = WorldGenerator.generate(10, 10)
    LifeGenerator.applyLife(0.6, world)
    val result = new WorldPrinter ('-', '0').print(world)
    Ok(views.html.index.render(result));
  }
}
