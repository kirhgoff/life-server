package org.kirhgoff.ap.core

import org.kirhgoff.ap.model.lifegame.LifeGameElement

//TODO Make it immutable, remove setElements
trait WorldModel {

  //TODO create separate class WorldGeometry?
  def width:Int
  def height:Int

  def getEnvironmentFor(element: Element): Environment
  def getElements:List[Element]
  def setElements(elements: List[Element])

//  def process(being:Element):Unit
//  def collectChanges(elementsToCreate: List[Element], elementsToRemove: List[Element]):Unit
//  def mergeChanges():Unit

  //TODO should not be here
  def printer:WorldPrinter
  def makeMerger: WorldModelMerger = ???

}

/**
 * Some useful methods
 */
abstract class WorldModel2D(val width:Int, val height:Int) extends WorldModel {
  var elements:List[Element] = List()

  override def getElements:List[Element] = elements
  override def setElements(elements: List[Element]) {
    //println (s"setElements: $elements")
    this.elements = elements
  }

  /**
   *
   * Indices of surrounding cells
   * -----
   * -012-
   * -3X4-
   * -567-
   * -----
   */
  def getEnvironmentFor(x:Int, y:Int):Environment = {
    getEnvironmentFor(getElementAt(x, y))
  }

  override def getEnvironmentFor(element: Element):Environment = {
    val e:LifeGameElement = element match {
      case x:LifeGameElement => x
      case _ => throw new IllegalArgumentException("Incorrect element type")
    }
    val x = e.x
    val y = e.y

    val elements = getElements
    val result = new Array[Boolean] (8)
    result(0) = elements(indexFor(x-1, y-1)).isAlive
    result(1) = elements(indexFor(x,   y-1)).isAlive
    result(2) = elements(indexFor(x+1, y-1)).isAlive

    result(3) = elements(indexFor(x-1, y)).isAlive
    result(4) = elements(indexFor(x+1, y)).isAlive

    result(5) = elements(indexFor(x-1, y+1)).isAlive
    result(6) = elements(indexFor(x,   y+1)).isAlive
    result(7) = elements(indexFor(x+1, y+1)).isAlive

    CloseSurroundings(result)
  }

  def getElementAt(x: Int, y: Int) = elements(indexFor(x, y))

  def indexFor(e:Element):Int = indexFor(e.x, e.y)
  def indexFor(x:Int, y:Int):Int = {
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

}
