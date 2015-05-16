package org.kirhgoff.ap.model.lotke_volterra

import akka.LifeActors
import org.kirhgoff.ap.core._

import scala.util.Random

class LotkeVolterraWorldModel(val width:Int, val height:Int) extends WorldModel {

  def process(being:Element) = {
    val environment:Environment = getEnvironmentFor(being)
    val strategy:Strategy = being.getStrategy (environment)

    strategy.apply(being, environment)
    val elementsToCreate:List[Element] = strategy.getCreatedElements
    val elementsToRemove:List[Element] = strategy.getRemovedElements
    val newState = strategy.getNewState

    //Merging is important
    // Will predator kill newborn prey? Or opposite?
    // Will moved prey kill newborn prey
    // do we see aging as new prey killing younger prey (age + 1)?
    //WTF? being.liveFurther(newState, newPosition)
    if (newState.isAlive) collectChanges(newState :: elementsToCreate, elementsToRemove)
    else  collectChanges(elementsToCreate, being :: elementsToRemove)
  }

  override def getWidth = width
  override def getHeight = height

  override def mergeChanges(): Unit = ???

  override def getElements: List[Element] = ???

  override def collectChanges(elementsToCreate: List[Element], elementsToRemove: List[Element]): Unit = ???

  override def getEnvironmentFor(element: Element): Environment = ???

  override def setElements(elements: List[Element]): Unit = ???


  override def printer: WorldPrinter = ???
}

object LotkeVolterraWorldModel {
  //Constants
  val InitialPreyEnergy = 1
  val InitialPredatorEnergy = 10
  val PreyMaturityAge = 2
  val PredatorMaturityAge = 2

  def main (args: Array[String]) {
    println("Starting Lotke-Volterra...")
    val world: WorldModel = new LotkaVolterraWorldGenerator(0.5, 0.5).generate(10, 10)

    println("-----------------------" +
      "Started with world:\n" +
      world.printer.toAsciiSquare(world))

    LifeActors.run(world, 10)
  }
}

class LotkaVolterraWorldGenerator(val lifeRatio: Double, val preyHunterRatio:Double) extends WorldGenerator {
  val random = new Random
  val hunterStrategy:Strategy = null
  val preyStrategy:Strategy = null

  def createWorld(width: Int, height: Int): WorldModel = {
    new LotkeVolterraWorldModel(width, height)
  }

  override def generateElement(x: Int, y: Int, model: WorldModel): Element = {
    import LotkeVolterraWorldModel._

    random.nextDouble() > lifeRatio match {
      case true => random.nextDouble > preyHunterRatio match {
        case true => new Being(Hunter, x, y, PredatorMaturityAge, InitialPredatorEnergy)
        case false => new Being(Prey, x, y, PreyMaturityAge, InitialPreyEnergy)
      }
      case false => new EmptyElement(x, y)
    }
  }
}
