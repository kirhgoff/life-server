package org.kirhgoff.ap.model.lifegame

import org.kirhgoff.ap.core.{Element, WorldModel, WorldGenerator}

import scala.util.Random

object LifeGameWorldGenerator extends WorldGenerator {

  val random = new Random

  def applyLife(percentOfLife:Double, world:WorldModel) = {
    world match {
      case world:LifeGameWorldModel => world.getElements.map(_.setAlive(random.nextDouble > percentOfLife))
      case _ => throw new IllegalArgumentException("Incorrect world type")
    }
  }

  def createWorld(width: Int, height: Int): WorldModel = {
    new LifeGameWorldModel(width, height)
  }

  def generateElement(x:Int, y:Int, world:WorldModel):Element = {
    world match {
      case w:LifeGameWorldModel => new LifeGameElement(x, y, w)
      case _ => throw new IllegalArgumentException("Incorrect world model class")
    }
  }
}
