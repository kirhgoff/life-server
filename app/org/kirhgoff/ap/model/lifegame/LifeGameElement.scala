package org.kirhgoff.ap.model.lifegame

class LifeGameElement(val x:Int, val y:Int, world:WorldModel) {
  var alive:Boolean = false

  def setAlive(alive: Boolean) = {this.alive = alive}
  def isAlive = alive

  def calculateNewState(value:WorldDimension):LifeGameElement = {
    val newState = new LifeGameElement (x, y, world)
    newState.setAlive(LifeGameElement.shouldBeAlive(alive, value))
    //println(s"$this newState=$newState sum=${Element.sum(value)}")
    newState
  }

  def calculateNewState():LifeGameElement = {
    calculateNewState(world.giveEnvironmentFor(x, y))
  }

  override def toString = s"E[$x, $y, ${if (alive) '0' else '-'}]"

  override def equals(other:Any):Boolean = other match {
    //TODO compare worldss
    case that : LifeGameElement => other.isInstanceOf[LifeGameElement] && x == that.x && y == that.y && isAlive == that.isAlive
    case _ => false
  }

  override def hashCode : Int = 41*(41*(41+x)+y) + LifeGameElement.toInt(isAlive)
}

object LifeGameElement {
  def toInt(b: Boolean) = if(b) 1 else 0
  def sum(value:WorldDimension) = value.surroundings.foldLeft(0)(_ + LifeGameElement.toInt(_))
  def shouldBeAlive(alive:Boolean, value:WorldDimension) = {
    val summa = sum(value)
    (alive && (summa == 2 || summa == 3)) || (!alive && summa == 3)
  }

}

