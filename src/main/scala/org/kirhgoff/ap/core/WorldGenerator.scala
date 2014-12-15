package org.kirhgoff.ap.core

import scala.util.Random

/**
 * Created by kirilllastovirya on 14/12/14.
 */
class WorldGenerator(x:Int, y:Int, percentOfLife:Double) {
  val world = new WorldModel(x, y)
  def generate:WorldModel = {
    val itemsCount:Int = calculateAmountOfLiveCells(x, y, percentOfLife)
    val elements = Seq.fill(itemsCount)(Random.nextInt).map(_ => generateElement(x, y)).toList
    world.setElements (elements)
    world
  }

  def generateElement(x:Int, y:Int):Element = new Element(x, y, world)

  def calculateAmountOfLiveCells(x: Int, y: Int, z: Double):Int = Math.round(x*y*z).toInt
}
