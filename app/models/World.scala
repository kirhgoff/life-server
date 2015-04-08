package models

import play.api.libs.json.Json

object World {

  case class World(content: String)

  implicit val worldWrites = Json.writes[World]
  implicit val worldReads = Json.reads[World]

  def apply(content:String) = World(content)
}
