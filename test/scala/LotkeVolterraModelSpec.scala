package scala

import org.kirhgoff.ap.core.WorldModel
import org.kirhgoff.ap.model.lotke_volterra.{Prey, LotkaVolterraWorldGenerator, LotkeVolterraWorldModel}
import org.specs2.mutable.Specification


class LotkeVolterraModelSpec extends Specification {

    implicit def convertLotkaVolterra(w:WorldModel):LotkeVolterraWorldModel = w.asInstanceOf[LotkeVolterraWorldModel]

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

    "Being" should {
      "be able to create random cells x" in {
        val world: WorldModel = new LotkaVolterraWorldGenerator().generate(3, 3)
        val prey = new Prey(1, 1, 0, 100)
        world.setElementAt(0, 0, new Prey(1, 1, 0, 100))
        world.setElementAt(1, 0, new Prey(1, 1, 0, 100))
        world.setElementAt(2, 0, new Prey(1, 1, 0, 100))

        //should never pick cell on first row
        for (i <- 0 to 100) {
          prey.randomFreeCell(world.getEnvironmentFor(prey)).y must beGreaterThan(0)
        }
        prey.isAlive shouldEqual true
      }

      "be able to create random cells y" in {
        val world: WorldModel = new LotkaVolterraWorldGenerator().generate(3, 3)
        val prey = new Prey(1, 1, 0, 100)
        world.setElementAt(0, 0, new Prey(1, 1, 0, 100))
        world.setElementAt(0, 1, new Prey(1, 1, 0, 100))
        world.setElementAt(0, 2, new Prey(1, 1, 0, 100))

        //should never pick cell on first row
        for (i <- 0 to 100) {
          prey.randomFreeCell(world.getEnvironmentFor(prey)).x must beGreaterThan(0)
        }

        prey.isAlive shouldEqual true
      }

    }
}
