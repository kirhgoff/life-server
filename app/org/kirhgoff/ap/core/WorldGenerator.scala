package org.kirhgoff.ap.core

import org.kirhgoff.ap.model.lifegame.LifeGenerator


abstract class WorldGenerator {

  def generate(width:Int, height:Int, lifeRatio:Double):WorldModel = {
    val world = createWorld(width, height)
    val elementsSeq = for {
      y <- 0 until height
      x <- 0 until width
    } yield generateElement(x, y, world)

    val elements = elementsSeq.toList
    world.setElements (elements)
    LifeGenerator.applyLife(lifeRatio, world)
    //println(s"WG: Generated elements ${elements.mkString(",")}")
    world
  }

  def createWorld(width: Int, height: Int):WorldModel

  def generateElement(x: Int, y: Int, model: WorldModel):Element


}
