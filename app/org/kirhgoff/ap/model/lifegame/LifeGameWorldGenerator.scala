package org.kirhgoff.ap.model.lifegame

import org.kirhgoff.ap.core.{Element, WorldModel, WorldGenerator}

object LifeGameWorldGenerator extends WorldGenerator {

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
