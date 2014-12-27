package org.kirhgoff.ap.core

class Element(val x:Int, val y:Int, world:WorldModel) {
  var alive:Boolean = false

  def setAlive(alive: Boolean) = {this.alive = alive}
  def isAlive = alive

  def calculateNewState(value:WorldDimension):Element = {
    val sum = value.aroundSense.foldLeft(0)(_ + toInt(_))

    val newState = new Element (x, y, world)
    newState.setAlive((alive && (sum == 2 || sum ==3)) || (!alive && sum == 3))
    //println(this + " newState=" + newState)
    newState
  }

  def calculateNewState:Element = calculateNewState(world.giveEnvironmentFor(x, y))

  private def toInt(b: Boolean) = if(b) 1 else 0
  override def toString = s"E[$x, $y, ${if (alive) '0' else '-'}]"

}

