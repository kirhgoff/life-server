package org.kirhgoff.ap.core

trait Element {
  //TODO remove from element
  def x:Int
  def y:Int

  def getStrategy(environment: Environment): Strategy
  def isAlive: Boolean
}

class EmptyElement(val x:Int, val y:Int) extends Element {
  override def getStrategy(environment: Environment): Strategy = ZeroStrategy(this)
  override def isAlive: Boolean = false
}



