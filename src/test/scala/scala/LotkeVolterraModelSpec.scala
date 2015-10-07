package scala

import akka.LifeActors
import org.junit.runner.RunWith
import org.kirhgoff.ap.core._
import org.kirhgoff.ap.model.lotke_volterra.{Hunter, Prey, LotkaVolterraWorldGenerator, LotkeVolterraWorldModel}
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class LotkeVolterraModelSpec extends Specification {

    implicit def convertLotkaVolterra(w:WorldModel):LotkeVolterraWorldModel = w.asInstanceOf[LotkeVolterraWorldModel]
    implicit def convertLotkaVolterra(e:Environment):ElementSurroundings = e.asInstanceOf[ElementSurroundings]

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
        for (i <- 0 to 50) {
          prey.randomCell(world.getEnvironmentFor(prey), !_.isAlive).get.
            y must beGreaterThan(0)
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
        for (i <- 0 to 50) {
          prey.randomCell(world.getEnvironmentFor(prey), !_.isAlive).get.
            x must beGreaterThan(0)
        }

        prey.isAlive shouldEqual true
      }
    }

  "Hunter" should {
    "be able to move in empty world" in {
      val world: WorldModel = new LotkaVolterraWorldGenerator().generate(3, 3)
      val hunter = new Hunter(1, 1)
      val env = world.getEnvironmentFor(hunter)

      hunter.canMove(env) should beTrue
      hunter.canBreed(env) should beFalse
      hunter.canFeed(env) should beFalse
    }

    "not be able to move in the world filled with preys and could breed" in {
      val world: WorldModel = new LotkaVolterraWorldGenerator().generate(3, 3)
      val hunter = new Hunter(1, 1)
      var env = world.getEnvironmentFor(hunter)
      env.around.map(e => world.setElementAt(new Prey(e.x, e.y)))

      env = world.getEnvironmentFor(hunter)
      hunter.canMove(env) should beFalse
      hunter.canBreed(env) should beFalse
      hunter.canFeed(env) should beTrue

      world.setElementAt(new Hunter(2, 2))
      hunter.canBreed(world.getEnvironmentFor(hunter)) should beFalse

      world.setElementAt(new EmptyElement(0, 2))
      hunter.canBreed(world.getEnvironmentFor(hunter)) should beTrue
    }

    "provide correct updates to world" in {
      val world: WorldModel = new LotkaVolterraWorldGenerator().generate(3, 3)
      val hunter = new Hunter(1, 1)
      world.setElementAt(new Prey(0, 0))

      hunter.apply(hunter, world.getEnvironmentFor(hunter))
      hunter.getRemovedElements.length shouldEqual 2
      hunter.getCreatedElements.length shouldEqual 1
    }
  }

  "Prey" should {
    "provide correct updates to world" in {
      val world: WorldModel = new LotkaVolterraWorldGenerator().generate(3, 3)
      val prey = new Prey(1, 1)
      world.setElementAt(prey)
      world.setElementAt(new Prey(0, 0))

      prey.apply(prey, world.getEnvironmentFor(prey))
      prey.getRemovedElements.length shouldEqual 0
      prey.getCreatedElements.length shouldEqual 1
    }
  }

  "LotkeVolterraWorldModel" should {
    "multiply number of preys in peaceful world" in {
      val world: WorldModel = new LotkaVolterraWorldGenerator().generate(3, 3)
      world.setElementAt(new Prey(1, 1))
      world.setElementAt(new Prey(0, 1))

      val listener = new TestListener
      LifeActors.run(world, listener, 3)
      //TODO remove me, its even doesnt work
      Thread.sleep(2000)
      val alive = listener.finalWorld.getElements.filter(_.isAlive)
      alive.length should be greaterThan(7)
    }
  }
}

class TestListener extends WorldModelListener  {
    var finalWorld:WorldModel = null

    override def worldUpdated(world: WorldModel): Unit = {
      this.finalWorld = world
      println("--------------")
      println(world.printer.toAsciiSquare(world))
    }
}

