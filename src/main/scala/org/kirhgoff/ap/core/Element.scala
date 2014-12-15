package org.kirhgoff.ap.core

/**
 * Created by kirilllastovirya on 14/12/14.
 */


class Element(val x:Int, val y:Int, world:WorldModel) {
  var alive:Boolean = false

  def setAlive(alive: Boolean) = {this.alive = alive}
  def isAlive = alive

  def calculateNewState = {
    val newState = new Element (x, y, world)

    val value:WorldDimension = world.giveEnvironmentFor(x, y)
    val sum = value.aroundSense.foldLeft(0)(_ + toInt(_))
    newState.setAlive(sum == 3)

    newState
  }

  private def toInt(b: Boolean) = if(b) 1 else 0

}

