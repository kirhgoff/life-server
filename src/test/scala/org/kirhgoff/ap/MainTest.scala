package org.kirhgoff.ap

import org.kirhgoff.ap.core._
import org.specs2.mutable.Specification

import scala.util.Random

class MainTestSpec extends Specification {
  //def actorRefFactory = system

  "WorldPrinter" should {

    "print an empty world" in {
      print(new WorldGenerator(1, 1, 0).generate) shouldEqual "0"
    }

    "print a small world" in {
      print(new WorldGenerator(2, 2, 0).generate) shouldEqual "00\n00"
    }
  }

  "WorldModel" should {
    "calculate indices correctly" in {
      val world = new WorldGenerator (2, 2, 0).generate

      world.indexFor(0, 0) shouldEqual 0
      world.indexFor(1, 0) shouldEqual 1
      world.indexFor(-1, 0) shouldEqual 1
      world.indexFor(0, 1) shouldEqual 2
      world.indexFor(0, -1) shouldEqual 2

      world.indexFor(-1, -1) shouldEqual 3
    }
  }

  "Element" should {
    "correctly process its state" in {
      val world = new WorldGenerator (10, 10, 0).generate
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

  def print(world:WorldModel) = new WorldPrinter(world, '1', '0').print(world.getElements)
  def make(aliveCount:Int) = {
    val result = Array.ofDim[Boolean](8)
    //todo find functional solution
    for (index <- 0 to 7) {
      result(index) = index < aliveCount
    }
    WorldDimension(Random.shuffle(result.toSeq).toArray[Boolean])
  }
}
