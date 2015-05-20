package org.kirhgoff.ap.model.lotke_volterra

import org.kirhgoff.ap.core._

import scala.collection.mutable

import org.kirhgoff.ap.model.lotke_volterra.LotkeVolterraWorldModel._

import scala.util.Random

abstract class Being(val x:Int, val y:Int, val maturityAge:Int, initialEnergy:Int)
  extends Element
  with Strategy {

  implicit def convertLotkaVolterra(e:Element):Being = e.asInstanceOf[Being]
  implicit def convertLotkaVolterra(e:Environment):ElementSurroundings = e.asInstanceOf[ElementSurroundings]
  implicit def boolean2Int(b: Boolean):Int = if(b) 1 else 0

  //In heart of every being lives Random
  val random = new Random()

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

  def randomCell(env: Environment, criteria:Element => Boolean = !_.isAlive): Option[Element] = {
    val cells = env.around.filter(criteria)
    if (cells.isEmpty) None
    else Some(cells(random.nextInt(cells.length)))
  }
}

class Hunter(x:Int, y:Int, maturityAge:Int = HunterMaturityAge, initialEnergy:Int = InitialHunterEnergy)
  extends Being (x, y, maturityAge, initialEnergy) {

  override def apply(value: Element, env: Environment): Unit = {
    //TODO excessive code, think about it
    if (!value.isInstanceOf[Hunter]) throw new IllegalStateException("Something is wrong")

    if (canFeed(env)) feed(env)
    else if (canBreed(env)) breed(env)
    else if(canMove(env)) move(env)
    else currentEnergy = currentEnergy - HunterEnergyDecoyPerTurn

    if (currentEnergy <= 0) removedElements :+ this
  }

  def canFeed (env:Environment) = {
    val preysAround = env.around.filter(_.isInstanceOf[Prey]).length
    preysAround > 0
  }

  def feed(env:Environment) = {
    val randomPrey:Element = randomCell(env, _.isInstanceOf[Prey]).get
    removedElements :+ randomPrey
    currentEnergy += CaloriesPerPrayEnergy * randomPrey.currentEnergy
  }

  def canBreed (env:Environment) = {
    val preysAround = env.around.filter(_.isInstanceOf[Prey]).length
    val matesAround = env.around.filter(_.isInstanceOf[Hunter]).length
    //there is a food, a mate and a free space
    preysAround > 0 && matesAround > 0 &&
      env.around.length - preysAround - matesAround > 0
  }

  //TODO let know the parent
  def breed(env:Environment) = {
    val randomFree:Element = randomCell(env, !_.isAlive).get
    createdElements :+ new Hunter(
      randomFree.x, randomFree.y, HunterMaturityAge, InitialPreyEnergy
    )
  }

  def canMove(env: Environment): Boolean = {
    val freeSpace = env.around.filter(!_.isAlive).length
    freeSpace > 0
  }

  //TODO keep trace
  def move(env: Environment) = {
    removedElements :+ this

    val cell:Element = randomCell(env, !_.isAlive).get
    createdElements :+ new Hunter(cell.x, cell.y, maturityAge,
      currentEnergy - HunterEnergyDecoyPerTurn)
  }
}

class Prey(x:Int, y:Int, maturityAge:Int = PreyMaturityAge, initialEnergy:Int = InitialHunterEnergy)
  extends Being (x, y, maturityAge, initialEnergy) {

  override def apply(value: Element, env: Environment): Unit = {
    if (!value.isInstanceOf[Prey]) throw new IllegalStateException("Something is wrong")

    //Like rabbits
    if (canBreed(env)) breed(env)
  }

  //Never change
  override def getNewState = this

  def canBreed(env:Environment) = {
    val aliveCount:Int = env.around.filter(_.isAlive).length
    aliveCount > 1 && aliveCount < 8
  }

  def breed (env:Environment) = {
    val randomFree:Element = randomCell(env, !_.isAlive).get
    createdElements :+ new Prey(
      randomFree.x, randomFree.y, PreyMaturityAge, InitialPreyEnergy
    )
  }

}





