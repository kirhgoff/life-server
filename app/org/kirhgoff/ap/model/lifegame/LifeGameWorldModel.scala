package org.kirhgoff.ap.model.lifegame

import java.awt.Graphics

import org.kirhgoff.ap.core._

import scala.util.Random

//-------------------------------
// Model
//---------------------------------

class LifeGameElementMerger extends ElementMerger () {
  override def createdElement(target: Element, update: Element): Element = {
    if (update.isAlive) update else target
  }
}

class LifeGameWorldModel(width:Int, height:Int) extends WorldModel2D(width, height) {
  val printer = new LifeGameWorldPrinter ('0', '-')
  val elementMerger = new LifeGameElementMerger ()
  override def makeMerger: WorldModelMerger = new WorldModel2DMerger(width, height, getElements, elementMerger)
}

//-------------------------------
// Generator
//---------------------------------

class LifeGameWorldGenerator(val percentOfLife:Double) extends WorldGenerator {

  val random = new Random

  def this() = this(2d)

  def createWorld(width: Int, height: Int): WorldModel = {
    new LifeGameWorldModel(width, height)
  }

  def generateElement(x:Int, y:Int, world:WorldModel):Element = {
    world match {
      case w:LifeGameWorldModel => new LifeGameElement(x, y, random.nextDouble > percentOfLife, w)
      case _ => throw new IllegalArgumentException("Incorrect world model class")
    }
  }
}

//-------------------------------
// Printer
//---------------------------------
class LifeGameWorldPrinter(val aliveSymbol:Char, val deadSymbol:Char) extends WorldPrinter {
  def renderElement(g:Graphics, element:Element, cellWidth:Double, cellHeight:Double) = {
    element match {
      case e: LifeGameElement =>  if (element.isAlive) g.fillRect((e.x*cellWidth).toInt, (e.y*cellHeight).toInt, cellWidth.toInt, cellHeight.toInt)
      case _ => throw new IllegalArgumentException("Incorrect element class")
    }
  }

  def renderElement(element:Element, asci: Array[Array[Char]]) = {
    asci(element.y)(element.x) = element match {
      case e: LifeGameElement if e.isAlive => aliveSymbol
      case e: LifeGameElement if !e.isAlive => deadSymbol
      case _ => throw new IllegalArgumentException("Incorrect element class")
    }
  }

}