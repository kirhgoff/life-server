package scala

import org.kirhgoff.ap.core.WorldModel
import org.kirhgoff.ap.model.lotke_volterra.LotkaVolterraWorldGenerator
import org.specs2.mutable.Specification


class LotkeVolterraModelSpec extends Specification {
    //def actorRefFactory = system

    "LotkaVolterraWorldGenerator" should {
      "generate one-cell world" in {
        val world: WorldModel = new LotkaVolterraWorldGenerator().generate(1, 1)
        world.getElements shouldNotEqual null
        world.getElements.length shouldEqual 1
        world.getElements(0) shouldNotEqual null
        world.getElements(0).x shouldEqual 0
        world.getElements(0).y shouldEqual 0
        world.getElements(0).isAlive shouldEqual false
      }
    }
}
