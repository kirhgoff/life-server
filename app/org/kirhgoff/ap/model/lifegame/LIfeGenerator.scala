package org.kirhgoff.ap.model.lifegame

import org.kirhgoff.ap.core.WorldModel

import scala.util.Random

object LifeGenerator {
  val random = new Random

  def applyLife(percentOfLife:Double, world:WorldModel) = {
    world match {
      case world:LifeGameWorldModel => world.getElements.map(_.setAlive(random.nextDouble > percentOfLife))
      case _ => throw new IllegalArgumentException("Incorrect world type")
    }
  }
//
//  def amountOfLiveCells(x: Int, y: Int, z: Double):Int = Math.round(x*y*z).toInt


}
