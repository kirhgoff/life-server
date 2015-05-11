package org.kirhgoff.ap.model.lifegame

class Element(val x:Int, val y:Int, world:WorldModel) {
  var alive:Boolean = false

  def setAlive(alive: Boolean) = {this.alive = alive}
  def isAlive = alive

  def calculateNewState(value:WorldDimension):Element = {
    val newState = new Element (x, y, world)
    newState.setAlive(Element.shouldBeAlive(alive, value))
    //println(s"$this newState=$newState sum=${Element.sum(value)}")
    newState
  }

  def calculateNewState():Element = {
    calculateNewState(world.giveEnvironmentFor(x, y))
  }

  override def toString = s"E[$x, $y, ${if (alive) '0' else '-'}]"

  override def equals(other:Any):Boolean = other match {
    //TODO compare worldss
    case that : Element => other.isInstanceOf[Element] && x == that.x && y == that.y && isAlive == that.isAlive
    case _ => false
  }

  override def hashCode : Int = 41*(41*(41+x)+y) + Element.toInt(isAlive)
}

object Element {
  def toInt(b: Boolean) = if(b) 1 else 0
  def sum(value:WorldDimension) = value.surroundings.foldLeft(0)(_ + Element.toInt(_))
  def shouldBeAlive(alive:Boolean, value:WorldDimension) = {
    val summa = sum(value)
    (alive && (summa == 2 || summa == 3)) || (!alive && summa == 3)
  }

}

