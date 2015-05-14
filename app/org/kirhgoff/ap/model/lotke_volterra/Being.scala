package org.kirhgoff.ap.model.lotke_volterra

import org.kirhgoff.ap.core.{Position, Element, Strategy}

class Being(currentEnergy: Int, maturityAge: Int, moveStrategy: Strategy, feedStrategy: Strategy, breedStrategy: Strategy, dieStrategy:Strategy) extends Element {
  var age: Int = 0

  def shouldDie = currentEnergy <= 0

  def canReproduce = age >= maturityAge

  override def liveFurther(element: Element, position: Position): Element = ???

  override def canFeed: Boolean = ???

  override def position: Position = ???

  override def dieStrategy: Strategy = ???

  override def canMove: Boolean = ???

  override def breedStrategy: Strategy = ???

  override def isAlive: Boolean = ???

  override def feedStrategy: Strategy = ???

  override def moveStrategy: Strategy = ???

  override def canBreed: Boolean = ???
}

