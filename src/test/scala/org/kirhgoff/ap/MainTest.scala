package org.kirhgoff.ap

import org.kirhgoff.ap.core.{WorldGenerator, WorldPrinter}
import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.http._
import StatusCodes._

class MainTestSpec extends Specification {
  //def actorRefFactory = system

  "WorldPrinter" should {

    "print an empty world" in {
      val worldGenerator = new WorldGenerator (1, 1, 0)
      val world = worldGenerator.generate

      val printer = new WorldPrinter(world, '1', '0')
      printer.print(world.getElements) shouldEqual "0"
    }

    "print an empty world" in {
      val world = new WorldGenerator (2, 2, 0).generate

      val printer = new WorldPrinter(world, '1', '0')
      printer.print(world.getElements) shouldEqual "00\n00"
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
}
