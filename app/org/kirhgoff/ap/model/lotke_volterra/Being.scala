package org.kirhgoff.ap.model.lotke_volterra

import org.kirhgoff.ap.core.{Element, Environment, Strategy}

sealed trait Kind
case object Prey extends Kind
case object Hunter extends Kind

class Being(val kind:Kind, val x:Int, val y:Int, val maturityAge:Int, initialEnergy:Int) extends Element {
  var currentEnergy: Int = initialEnergy
  var age: Int = 0
  val strategy: Strategy = kind match {
    case Prey => new PreyStrategy()
    case Hunter => new HunterStrategy()
  }


  override def isAlive: Boolean = currentEnergy > 0

  override def getStrategy(environment: Environment): Strategy = strategy
}

