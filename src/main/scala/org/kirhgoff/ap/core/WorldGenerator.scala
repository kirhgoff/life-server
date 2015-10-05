package org.kirhgoff.ap.core


abstract class WorldGenerator {

  def generate(width:Int, height:Int):WorldModel = {
    val world = createWorld(width, height)
    val elementsSeq = for {
      y <- 0 until height
      x <- 0 until width
    } yield generateElement(x, y, world)

    val elements = elementsSeq.toList
    world.setElements (elements)
    //println(s"WG: Generated elements ${elements.mkString(",")}")
    world
  }

  def createWorld(width: Int, height: Int):WorldModel

  def generateElement(x: Int, y: Int, model: WorldModel):Element


}
