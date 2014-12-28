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
    val sum = value.surroundings.foldLeft(0)(_ + toInt(_))
    //Conway's life rules
//    alive match {
//      case false if sum == 3 => newState.setAlive(true)
//      case _ if sum == 2 || sum == 3 => newState.setAlive(true)
//      case _ => newState.setAlive(false)
//    }

    newState.setAlive(alive && (sum == 2 || sum == 3) || !alive && (sum == 3))
    //println(this + " newState=" + newState)
    newState
  }

  private def toInt(b: Boolean) = if(b) 1 else 0
  override def toString = s"E[$x, $y, ${if (alive) '0' else '-'}]"

}

