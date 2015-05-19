package org.kirhgoff.ap.model.lotke_volterra

import org.kirhgoff.ap.core.{Element, Environment, Strategy}

abstract class Being(val x:Int, val y:Int, val maturityAge:Int, initialEnergy:Int) extends Element {
  var currentEnergy: Int = initialEnergy
  var age: Int = 0
  //TODO check that it is not created every time
  def strategy: Strategy

  override def isAlive: Boolean = currentEnergy > 0
  override def getStrategy(environment: Environment): Strategy = strategy
}

class Hunter(x:Int, y:Int, maturityAge:Int, initialEnergy:Int)
  extends Being (x, y, maturityAge, initialEnergy) {
    def strategy = new HunterStrategy()
}

class Prey(x:Int, y:Int, maturityAge:Int, initialEnergy:Int)
  extends Being (x, y, maturityAge, initialEnergy) {
  def strategy = new PreyStrategy()
}


class HunterStrategy extends Strategy {
  override def apply(value: Element, environment: Environment): Unit = ???
  override def getRemovedElements: List[Element] = ???
  override def getNewState: Element = ???
  override def getCreatedElements: List[Element] = ???
}

class PreyStrategy extends Strategy {
  override def apply(value: Element, environment: Environment): Unit = ???
  override def getRemovedElements: List[Element] = ???
  override def getNewState: Element = ???
  override def getCreatedElements: List[Element] = ???
}




