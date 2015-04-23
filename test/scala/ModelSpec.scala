package scala

import models.StartCommand.StartCommand
import org.specs2.mutable.Specification
import play.api.libs.json.Json

class ModelSpec extends Specification {

  "Model should be able to" should {
    "read/write as Json" in {
      val json = Json.parse("{\"width\":\"10\", \"height\":\"10\"}")
      val command = json.as[StartCommand]
      command.width shouldEqual "10"
      command.height shouldEqual "10"
    }
  }
}