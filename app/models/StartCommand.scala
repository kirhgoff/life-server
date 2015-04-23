package models

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.data.validation._

object StartCommand {
  case class StartCommand(width: Int, height: Int, iterations:Option[Int])

  implicit val startCommandWrites: Writes[StartCommand] = (
    (__ \ "width").write[Int] and
    (__ \ "height").write[Int] and
    (__ \ "iterations").writeNullable[Int]
  )(unlift(StartCommand.unapply))

  implicit val startCommandReads: Reads[StartCommand] = (
    (__ \ "width").read[Int] and
    (__ \ "height").read[Int] and
    (__ \ "iterations").readNullable[Int]
  )(StartCommand.apply _)

}
