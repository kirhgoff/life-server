package org.kirhgoff.ap.core

object WorldGenerator {

  def generate(width:Int, height:Int):WorldModel = {
    val world = new WorldModel(width, height)
    val elementsSeq = for {
      x <- 0 until width
      y <- 0 until height
    } yield generateElement(x, y, world)

    val elements = elementsSeq.toList
    world.setElements (elements)
    //println(s"WG: Generated elements ${elements.mkString(",")}")
    world
  }

  def generateElement(x:Int, y:Int, world:WorldModel):Element = new Element(x, y, world)

}
