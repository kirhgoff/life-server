package org.kirhgoff.ap.model.lifegame

import scala.util.Random

object LifeGenerator {
  val random = new Random

  def applyLife(percentOfLife:Double, world:LifeGameWorldModel) = world.getElements.map(_.setAlive(random.nextDouble > percentOfLife))
//
//  def amountOfLiveCells(x: Int, y: Int, z: Double):Int = Math.round(x*y*z).toInt


}
