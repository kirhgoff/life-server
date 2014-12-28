package org.kirhgoff.ap

import org.kirhgoff.ap.core.{WorldModel, Element, WorldGenerator, WorldPrinter}
import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.http._
import StatusCodes._

class MainTestSpec extends Specification {
  //def actorRefFactory = system

  "WorldPrinter" should {

    "print an empty world" in {
      print(WorldGenerator.generate(1, 1)) shouldEqual "0"
    }

    "print a small world" in {
      print(WorldGenerator.generate(2, 2)) shouldEqual "00\n00"
    }
  }

  "WorldModel" should {
    "calculate indices correctly" in {
      val world = WorldGenerator.generate (2, 2)

      world.indexFor(0, 0) shouldEqual 0
      world.indexFor(1, 0) shouldEqual 1
      world.indexFor(-1, 0) shouldEqual 1
      world.indexFor(0, 1) shouldEqual 2
      world.indexFor(0, -1) shouldEqual 2

      world.indexFor(-1, -1) shouldEqual 3
    }
  }

  "Element" should {
    "live as brick" in {
      val world = WorldGenerator.generate (4, 4)
      world.getElementAt(1, 1).setAlive(true)
      world.getElementAt(2, 1).setAlive(true)
      world.getElementAt(1, 2).setAlive(true)
      world.getElementAt(2, 2).setAlive(true)

      print(world) shouldEqual "0000\n0110\n0110\n0000"

      world.setElements(world.getElements.map(_.calculateNewState))

      print(world) shouldEqual "0000\n0110\n0110\n0000"
    }

    "live as beehive" in {
      val world = WorldGenerator.generate (6, 4)
      world.getElementAt(1, 1).setAlive(true)
      world.getElementAt(2, 1).setAlive(true)
      world.getElementAt(1, 2).setAlive(true)
      world.getElementAt(2, 2).setAlive(true)

      print(world) shouldEqual "0000\n0110\n0110\n0000"

      world.setElements(world.getElements.map(_.calculateNewState))

      print(world) shouldEqual "0000\n0110\n0110\n0000"
    }

    "momentarily die" in {
      val world = WorldGenerator.generate (3, 3)
      world.getElementAt(1, 1).setAlive(true)

      print(world) shouldEqual "000\n010\n000"

      world.setElements(world.getElements.map(_.calculateNewState))

      print(world) shouldEqual "000\n000\n000"
    }

    "oscilate" in {
      val world = WorldGenerator.generate (5, 5)
      world.getElementAt(1, 2).setAlive(true)
      world.getElementAt(2, 2).setAlive(true)
      world.getElementAt(3, 2).setAlive(true)

      print(world) shouldEqual "00000\n00000\n01110\n00000\n00000"

      world.setElements(world.getElements.map(_.calculateNewState))

      print(world) shouldEqual "00000\n00100\n00100\n00100\n00000"
    }

  }

  def print(world:WorldModel) = new WorldPrinter('1', '0').print(world)
}
