package org.kirhgoff.ap.core

import scala.util.Random

class WorldGenerator(width:Int, height:Int, percentOfLife:Double) {
  val world = new WorldModel(width, height)
  def generate:WorldModel = {
    //val itemsCount:Int = calculateAmountOfLiveCells(x, y, percentOfLife)
    val elements:List[Element] = (List.range(0, width-1), List.range(0, height -1)).zipped  map {
      (x:Int, y:Int) => generateElement(x, y)
    }
    println(elements.mkString(","))
    world.setElements (elements)
    world
  }

  def generateElement(x:Int, y:Int):Element = new Element(x, y, world)

  def calculateAmountOfLiveCells(x: Int, y: Int, z: Double):Int = Math.round(x*y*z).toInt
}
