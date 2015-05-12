package org.kirhgoff.ap.model.lifegame

import org.kirhgoff.ap.core.{Element, Environment, WorldModel}

case class WorldDimension (surroundings:Array[Boolean]) extends Environment

class LifeGameWorldModel(val width:Int, val height:Int) extends WorldModel {
  val worldPrinter = new LifeGameWorldPrinter ('0', '-')

  def printer = worldPrinter

  def getElementAt(x: Int, y: Int) = elements(indexFor(x, y))

  var elements:List[LifeGameElement] = List()

  /**
   *
   * Indices of surrounding cells
   * -----
   * -012-
   * -3X4-
   * -567-
   * -----
   */
  override def getEnvironmentFor(element: Element):Environment = {
    val e:LifeGameElement = element match {
      case x:LifeGameElement => x
      case _ => throw new IllegalArgumentException("Incorrect element type")
    }
    val x = e.x
    val y = e.y

    val result = new Array[Boolean] (8)
    result(0) = elements(indexFor(x-1, y-1)).isAlive
    result(1) = elements(indexFor(x,   y-1)).isAlive
    result(2) = elements(indexFor(x+1, y-1)).isAlive

    result(3) = elements(indexFor(x-1, y)).isAlive
    result(4) = elements(indexFor(x+1, y)).isAlive

    result(5) = elements(indexFor(x-1, y+1)).isAlive
    result(6) = elements(indexFor(x,   y+1)).isAlive
    result(7) = elements(indexFor(x+1, y+1)).isAlive

    WorldDimension(result)
  }

  //Move to trait
  def indexFor(x:Int, y:Int) = {
    var newX  = x
    var newY = y
    if (x < 0) {
      newX = width + x
    } else if (x >= width) {
      newX = x % width
    }
    if (y < 0) {
      newY = height + y
    } else if (y >= height) {
      newY = y % height
    }

    newX + newY * width
  }


  override def setElements(elements: List[Element]) {
    //println (s"setElements: $elements")
    elements match {
      case elems:List[LifeGameElement] => this.elements = elems
      case _ => throw new IllegalArgumentException("Incorrect elements type")
    }
  }

  def getElements:List[LifeGameElement] = elements

  override def collectChanges(elementsToCreate: List[Element], elementsToRemove: List[Element]): Unit = {}
  override def mergeChanges(): Unit = {}
  override def process(being: Element): Unit = {}
}
