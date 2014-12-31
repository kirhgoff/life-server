package org.kirhgoff.ap

import org.kirhgoff.ap.core.{WorldModel, Element, WorldGenerator, WorldPrinter}
import org.kirhgoff.ap.core._
import org.specs2.mutable.Specification

import scala.util.Random

class MainTestSpec extends Specification {
  //def actorRefFactory = system

  "WorldGenerator" should {
    "generate one-cell world" in {
      val world = WorldGenerator.generate(1, 1)
      world.getElements shouldNotEqual null
      world.getElements.length shouldEqual 1
      world.getElements(0) shouldNotEqual null
      world.getElements(0).x shouldEqual 0
      world.getElements(0).y shouldEqual 0
      world.getElements(0).isAlive shouldEqual false
    }

    "generate bigger worlds" in {
      val world = WorldGenerator.generate(2, 2)
      world.getElements shouldNotEqual null
      world.getElements.length shouldEqual 4
      world.getElements(3) shouldNotEqual null
      world.getElements(3).x shouldEqual 1
      world.getElements(3).y shouldEqual 1
      world.getElements(3).isAlive shouldEqual false
    }

  }

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

    "give surroundings correctly" in {
      val world = WorldGenerator.generate (3, 3)
      world.getElementAt(1, 1).setAlive(true)

      Element.sum(world.giveEnvironmentFor(1, 1)) shouldEqual 0
      Element.sum(world.giveEnvironmentFor(0, 1)) shouldEqual 1
      Element.sum(world.giveEnvironmentFor(2, 2)) shouldEqual 1
    }

    "give surroundings correctly 2" in {
      val world = WorldGenerator.generate (5, 5)
      world.getElementAt(1, 2).setAlive(true)
      world.getElementAt(2, 2).setAlive(true)
      world.getElementAt(3, 2).setAlive(true)

      Element.sum(world.giveEnvironmentFor(1, 2)) shouldEqual 1
      Element.sum(world.giveEnvironmentFor(2, 2)) shouldEqual 2
      Element.sum(world.giveEnvironmentFor(3, 2)) shouldEqual 1

      Element.sum(world.giveEnvironmentFor(2, 1)) shouldEqual 3
      Element.sum(world.giveEnvironmentFor(2, 3)) shouldEqual 3
    }

    "order elements correctly in array" in {
      val world = WorldGenerator.generate (2, 2)
      val elements = world.getElements
      elements.length shouldEqual 4
      elements(0) shouldEqual new Element(0, 0, world)
      elements(1) shouldEqual new Element(1, 0, world)
      elements(2) shouldEqual new Element(0, 1, world)
      elements(3) shouldEqual new Element(1, 1, world)
    }

    "return correct elements" in {
      val world = WorldGenerator.generate (2, 2)
      world.getElementAt(0, 1).setAlive(true)
      val elem = world.getElementAt(0, 1)
      elem.x shouldEqual 0
      elem.y shouldEqual 1
      elem.isAlive shouldEqual true

      val elem2 = world.getElementAt(1, 0)
      elem2.x shouldEqual 1
      elem2.y shouldEqual 0
      elem2.isAlive shouldEqual false

      val elem3 = elem.calculateNewState()
      elem3.x shouldEqual 0
      elem3.y shouldEqual 1
      elem3.isAlive shouldEqual false
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

      val elem: Element = world.getElementAt(1, 2)
      val value = world.giveEnvironmentFor(1, 2)
      elem.isAlive shouldEqual true
      Element.sum(value) shouldEqual 1
      Element.shouldBeAlive(true, value) shouldEqual false

      elem.calculateNewState().isAlive shouldEqual false
      world.getElementAt(2, 2).calculateNewState().isAlive shouldEqual true
      world.getElementAt(3, 2).calculateNewState().isAlive shouldEqual false
      world.getElementAt(2, 1).calculateNewState().isAlive shouldEqual true
      world.getElementAt(2, 2).calculateNewState().isAlive shouldEqual true
      world.getElementAt(2, 3).calculateNewState().isAlive shouldEqual true

      world.setElements(world.getElements.map(_.calculateNewState()))

      print(world) shouldEqual "00000\n00100\n00100\n00100\n00000"
    }

    "correctly process its state" in {
      val world = WorldGenerator.generate(10, 10)
      val element = new Element(50, 50, world)

      element.setAlive(true)
      element.calculateNewState (make(0)).isAlive shouldEqual false
      element.calculateNewState (make(1)).isAlive shouldEqual false
      element.calculateNewState (make(2)).isAlive shouldEqual true //Lives
      element.calculateNewState (make(3)).isAlive shouldEqual true //lives
      element.calculateNewState (make(4)).isAlive shouldEqual false
      element.calculateNewState (make(5)).isAlive shouldEqual false
      element.calculateNewState (make(6)).isAlive shouldEqual false
      element.calculateNewState (make(7)).isAlive shouldEqual false

      element.setAlive(false)
      element.calculateNewState (make(0)).isAlive shouldEqual false
      element.calculateNewState (make(1)).isAlive shouldEqual false
      element.calculateNewState (make(2)).isAlive shouldEqual false
      element.calculateNewState (make(3)).isAlive shouldEqual true //becomes alive
      element.calculateNewState (make(4)).isAlive shouldEqual false
      element.calculateNewState (make(5)).isAlive shouldEqual false
      element.calculateNewState (make(6)).isAlive shouldEqual false
      element.calculateNewState (make(7)).isAlive shouldEqual false

    }
  }

  def print(world:WorldModel) = new WorldPrinter('1', '0').print(world)

  def make(aliveCount:Int) = {
    val result = Array.ofDim[Boolean](8)
    //todo find functional solution
    for (index <- 0 to 7) {
      result(index) = index < aliveCount
    }
    WorldDimension(Random.shuffle(result.toSeq).toArray[Boolean])
  }
}
