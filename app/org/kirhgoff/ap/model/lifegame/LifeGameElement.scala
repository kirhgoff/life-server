package org.kirhgoff.ap.model.lifegame

import org.kirhgoff.ap.core._


class LifeGameElement(val x:Int, val y:Int, var alive:Boolean, world:LifeGameWorldModel) extends Element{

  def this(x:Int, y:Int, world:LifeGameWorldModel) = this(x, y, false, world)

  def isAlive = alive
  def setAlive(alive:Boolean) = this.alive = alive

  def calculateNewState(value:Environment):LifeGameElement = {
    value match {
      case value:CloseSurroundings => {
        val newState = new LifeGameElement (x, y, LifeGameElement.shouldBeAlive(alive, value), world)
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
    //TODO compare worlds
    case that : LifeGameElement => other.isInstanceOf[LifeGameElement] && x == that.x && y == that.y && isAlive == that.isAlive
    case _ => false
  }

  override def hashCode : Int = 41*(41*(41+x)+y) + LifeGameElement.toInt(isAlive)

  //TODO create real strategy
  override def getStrategy(environment: Environment): Strategy = DoNothingStrategy(this)
}

object LifeGameElement {
  def toInt(b: Boolean) = if(b) 1 else 0
  def sum(value:CloseSurroundings) = value.surroundings.foldLeft(0)(_ + LifeGameElement.toInt(_))

  def shouldBeAlive(alive:Boolean, value:CloseSurroundings) = {
    val summa = sum(value)
    (alive && (summa == 2 || summa == 3)) || (!alive && summa == 3)
  }
}

