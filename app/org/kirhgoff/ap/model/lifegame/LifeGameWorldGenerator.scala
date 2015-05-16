package org.kirhgoff.ap.model.lifegame

import org.kirhgoff.ap.core.{Element, WorldModel, WorldGenerator}

import scala.util.Random


class LifeGameWorldGenerator(val percentOfLife:Double) extends WorldGenerator {

  val random = new Random

  def createWorld(width: Int, height: Int): WorldModel = {
    new LifeGameWorldModel(width, height)
  }

  def generateElement(x:Int, y:Int, world:WorldModel):Element = {
    world match {
      case w:LifeGameWorldModel => new LifeGameElement(x, y, random.nextDouble > percentOfLife, w)
      case _ => throw new IllegalArgumentException("Incorrect world model class")
    }
  }
}
