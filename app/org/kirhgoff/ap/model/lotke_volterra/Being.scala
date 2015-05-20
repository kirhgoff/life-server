package org.kirhgoff.ap.model.lotke_volterra

import org.kirhgoff.ap.core.{CloseSurroundings, Element, Environment, Strategy}

import scala.collection.mutable

import org.kirhgoff.ap.model.lotke_volterra.LotkeVolterraWorldModel._

abstract class Being(val x:Int, val y:Int, val maturityAge:Int, initialEnergy:Int)
  extends Element
  with Strategy {

  implicit def convertLotkaVolterra(e:Element):Being = e.asInstanceOf[Being]
  implicit def convertLotkaVolterra(e:Environment):CloseSurroundings = e.asInstanceOf[CloseSurroundings]
  implicit def boolean2Int(b: Boolean):Int = if(b) 1 else 0

  var currentEnergy: Int = initialEnergy
  var age: Int = 0
  val removedElements = new mutable.MutableList[Element]
  val createdElements = new mutable.MutableList[Element]
  var newState = this

  def strategy: Strategy = this

  override def isAlive: Boolean = currentEnergy > 0
  override def getStrategy(environment: Environment): Strategy = strategy

  def getNewState: Element = newState
  def getRemovedElements: List[Element] = removedElements.toList
  def getCreatedElements: List[Element] = createdElements.toList

  def sum (value:CloseSurroundings) = value.surroundings.foldLeft(0)(_ + boolean2Int(_))

  def feed(environment: Environment) = {}
  def breed(environment: Environment) = {}
  def move(environment: Environment) = {}

  def canFeed(env:Environment): Boolean = false
  def canBreed(environment: Environment): Boolean = false
}

class Hunter(x:Int, y:Int, maturityAge:Int, initialEnergy:Int)
  extends Being (x, y, maturityAge, initialEnergy) {


  override def apply(value: Element, env: Environment): Unit = {
    //TODO excessive code, think about it
    if (!value.isInstanceOf[Hunter]) throw new IllegalStateException("Something is wrong")

    if (canFeed(env)) feed(env)
    else if (canBreed(env)) breed(env)
    else move(env)
  }
  
}

class Prey(x:Int, y:Int, maturityAge:Int, initialEnergy:Int)
  extends Being (x, y, maturityAge, initialEnergy) {
  override def apply(value: Element, env: Environment): Unit = {
    if (!value.isInstanceOf[Prey]) throw new IllegalStateException("Something is wrong")

    if (canBreed(env)) breed(env)
  }

  override def canBreed(env:Environment) = {
    val summa:Int = sum(env)
    summa > 1 && summa < 8
  }

  def randomFreeCell(env: Environment): Element = {
    this
  }

  override def breed (env:Environment) = {
    val randomFree:Element = randomFreeCell(env)
    createdElements :+ new Prey(randomFree.x, randomFree.y, 0, InitialPreyEnergy)
  }


}





