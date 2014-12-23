package org.kirhgoff.ap.core

import scala.util.Random

class WorldGenerator(width:Int, height:Int, percentOfLife:Double) {
  val random = new Random
  val world = new WorldModel(width, height)

  def generate:WorldModel = {
    val elementsSeq = for {
      x <- 0 until width
      y <- 0 until height
    } yield generateElement(x, y)

    val elements = elementsSeq.toList
    applyLife(elements)
    world.setElements (elements)
    println(s"WG: Generated elements ${elements.mkString(",")}")
    world
  }

  def generateElement(x:Int, y:Int):Element = new Element(x, y, world)

  def amountOfLiveCells(x: Int, y: Int, z: Double):Int = Math.round(x*y*z).toInt

  def applyLife(elements:List[Element]) = elements.map(_.setAlive(random.nextDouble > percentOfLife))
}
