package org.kirhgoff.ap.core

import scala.util.Random

object LifeGenerator {
  val random = new Random

  def applyLife(percentOfLife:Double, world:WorldModel) = world.getElements.map(_.setAlive(random.nextDouble > percentOfLife))
//
//  def amountOfLiveCells(x: Int, y: Int, z: Double):Int = Math.round(x*y*z).toInt


}
