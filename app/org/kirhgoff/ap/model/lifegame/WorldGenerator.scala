package org.kirhgoff.ap.model.lifegame

object WorldGenerator {

  def generate(width:Int, height:Int):LifeGameWorldModel = {
    val world = new LifeGameWorldModel(width, height)
    val elementsSeq = for {
      y <- 0 until height
      x <- 0 until width
    } yield generateElement(x, y, world)

    val elements = elementsSeq.toList
    world.setElements (elements)
    //println(s"WG: Generated elements ${elements.mkString(",")}")
    world
  }

  def generateElement(x:Int, y:Int, world:LifeGameWorldModel):LifeGameElement = new LifeGameElement(x, y, world)
}