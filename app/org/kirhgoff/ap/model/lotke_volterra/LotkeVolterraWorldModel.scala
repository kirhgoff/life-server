package org.kirhgoff.ap.model.lotke_volterra

import java.awt.Graphics

import akka.LifeActors
import org.kirhgoff.ap.core._
import org.kirhgoff.ap.model.lifegame.LifeGameElement

import scala.util.Random

//-------------------------------
// Model
//---------------------------------

class LotkeVolterraElementMerger extends ElementMerger {
  override def createdElement(target: Element, update: Element): Element = {
    (target, update) match {
      case (_: EmptyElement, _: Element) => update
      case (_: Hunter, _: Prey) => target
      case (_: Prey, _: Hunter) => update
      case (one: Prey, another: Prey) => whoHaveMoreEnergy(one, another)
      case (one: Hunter, another: Hunter) => whoHaveMoreEnergy(one, another)
      case _ => throw new IllegalArgumentException("Can be only Empty, Prey or Hunter!")
    }
  }

  def whoHaveMoreEnergy(one: Being, another: Being): Being = {
    if (one.currentEnergy > another.currentEnergy) one else another
  }
}

class LotkeVolterraWorldModel(width:Int, height:Int) extends WorldModel2D(width, height) {
  val printer: WorldPrinter = new LotkeVolterraWorldPrinter('p', 'H', ' ')
  val elementMerger = new LotkeVolterraElementMerger()

  override def makeMerger = new WorldModel2DMerger(width, height, getElements, elementMerger)

  override def getEnvironmentFor(element: Element): Environment = {
    ElementSurroundings(getSurroundingElements(element))
  }
}
  //-------------------------------
// Generator
//---------------------------------

class LotkaVolterraWorldGenerator(val lifeRatio: Double, val preyHunterRatio:Double) extends WorldGenerator {
  val random = new Random
  val hunterStrategy:Strategy = null
  val preyStrategy:Strategy = null

  def this() = this(2d, 2d)

  def createWorld(width: Int, height: Int): WorldModel = {
    new LotkeVolterraWorldModel(width, height)
  }

  override def generateElement(x: Int, y: Int, model: WorldModel): Element = {
    import LotkeVolterraWorldModel._

    random.nextDouble() > lifeRatio match {
      case true => random.nextDouble > preyHunterRatio match {
        case true => new Hunter (x, y, HunterMaturityAge, InitialHunterEnergy)
        case false => new Prey (x, y, PreyMaturityAge, InitialPreyEnergy)
      }
      case false => new EmptyElement(x, y)
    }
  }
}

//-------------------------------
// Printer
//---------------------------------

class LotkeVolterraWorldPrinter(val preySymbol:Char, val hunterSymbol:Char, emptySymbol:Char) extends WorldPrinter {
  def renderElement(g:Graphics, element:Element, cellWidth:Double, cellHeight:Double) = {
    element match {
      case e: LifeGameElement =>  if (element.isAlive) {
        g.fillRect((e.x*cellWidth).toInt, (e.y*cellHeight).toInt, cellWidth.toInt, cellHeight.toInt)
        //TODO draw symbol or change color for every class
      }
      case _ => throw new IllegalArgumentException("Incorrect element class")
    }
  }

  def renderElement(element:Element, asci: Array[Array[Char]]) = {
    asci(element.y)(element.x) = element match {
      case _:Prey => preySymbol
      case _:Hunter => hunterSymbol
      case _:EmptyElement => emptySymbol
      case _ => throw new IllegalArgumentException("Incorrect element class")
    }
  }

}


//-------------------------------
// Main
//---------------------------------

object LotkeVolterraWorldModel {

  //Constants
  val InitialPreyEnergy = 1
  val InitialHunterEnergy = 10

  val PreyMaturityAge = 2
  val HunterMaturityAge = 2

  val HunterBreedThreshold = 1
  val PreyBreedProbability = 1

  val CaloriesPerPrayEnergy = 1
  val HunterEnergyDecoyPerTurn = 1

  //Starter parameters
  val Width = 5
  val Height = 5
  val Iterations: Int = 3
  val LifeRatio: Double = 0.5
  val PreyHunterRatio: Double = 0.5

  def main (args: Array[String]) {
    println("Starting Lotke-Volterra...")
    val world: WorldModel = new LotkaVolterraWorldGenerator(LifeRatio, PreyHunterRatio).generate(Width, Height)
    val printer = world.printer
    val listener = new WorldModelListener {
      override def worldUpdated(world: WorldModel): Unit = {
        println("________________________________")
        println(printer.toAsciiSquare(world))
      }
    }
    println("-----------------------" +
      s"Started with world: ${}\n" +
      world.printer.toAsciiSquare(world))

    LifeActors.run(world, listener, Iterations)
  }
}

