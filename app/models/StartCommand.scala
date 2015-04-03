package models

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.data.validation._

object StartCommand {

  case class StartCommand(width: String, height: String)

  // implicit val startCommandWrites = Json.writes[StartCommand]

  // implicit val startCommandReads:Reads[StartCommand] = (
  //       (JsPath \ "width").read[Integer] and
  //       (JsPath \ "height").read[Integer]
  //   )(StartCommand.apply _)
  implicit val startCommandWrites = Json.writes[StartCommand]
  implicit val startCommandReads = Json.reads[StartCommand]

}
