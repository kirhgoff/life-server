package org.kirhgoff.ap.model.lifegame

import org.kirhgoff.ap.core._

class LifeGameElement(val x:Int, val y:Int, world:LifeGameWorldModel) extends Element{
  var alive:Boolean = false

  def setAlive(alive: Boolean) = {this.alive = alive}
  def isAlive = alive

  def calculateNewState(value:Environment):LifeGameElement = {
    value match {
      case value:WorldDimension => {
        val newState = new LifeGameElement (x, y, world)
        newState.setAlive(LifeGameElement.shouldBeAlive(alive, value))
        //println(s"$this newState=$newState sum=${Element.sum(value)}")
        newState
      }
      case _ => throw new IllegalArgumentException("Incorrect environment type")
    }
  }

  def calculateNewState():LifeGameElement = {
    calculateNewState(world.getEnvironmentFor(this))
  }

  override def toString = s"E[$x, $y, ${if (alive) '0' else '-'}]"

  override def equals(other:Any):Boolean = other match {
    //TODO compare worldss
    case that : LifeGameElement => other.isInstanceOf[LifeGameElement] && x == that.x && y == that.y && isAlive == that.isAlive
    case _ => false
  }

  override def hashCode : Int = 41*(41*(41+x)+y) + LifeGameElement.toInt(isAlive)

  //Default overrides
  override def liveFurther(element: Element, position: Position): Element = this
  override def canFeed: Boolean = true
  override def position: Position = new Position(){}
  override def dieStrategy: Strategy = ZeroStrategy(this)
  override def breedStrategy: Strategy = ZeroStrategy(this)
  override def feedStrategy: Strategy = ZeroStrategy(this)
  override def moveStrategy: Strategy = ZeroStrategy(this)
  override def canBreed: Boolean = true
  override def shouldDie: Boolean = false
  override def canMove: Boolean = false


}

object LifeGameElement {
  def toInt(b: Boolean) = if(b) 1 else 0
  def sum(value:WorldDimension) = value.surroundings.foldLeft(0)(_ + LifeGameElement.toInt(_))
  def shouldBeAlive(alive:Boolean, value:WorldDimension) = {
    val summa = sum(value)
    (alive && (summa == 2 || summa == 3)) || (!alive && summa == 3)
  }

}

