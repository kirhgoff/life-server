package scala

import org.kirhgoff.ap.model.lifegame.LifeGameWorldGenerator
import org.specs2.mutable.Specification


class LotkeVolterraModelSpec extends Specification {
    //def actorRefFactory = system

    "WorldGenerator" should {
      "generate one-cell world" in {
        val world = LifeGameWorldGenerator.generate(1, 1)
        world.getElements shouldNotEqual null
        world.getElements.length shouldEqual 1
        world.getElements(0) shouldNotEqual null
        world.getElements(0).x shouldEqual 0
        world.getElements(0).y shouldEqual 0
        world.getElements(0).isAlive shouldEqual false
      }

}
