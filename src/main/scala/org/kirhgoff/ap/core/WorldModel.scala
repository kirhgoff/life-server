package org.kirhgoff.ap.core

import org.kirhgoff.ap.model.lifegame.LifeGameElement

import scala.collection.mutable

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
  var elements:mutable.MutableList[Element] = mutable.MutableList()

  override def getElements:List[Element] = elements.toList
  override def setElements(elements: List[Element]) {
    //println (s"setElements: $elements")
    this.elements = mutable.MutableList() ++ elements
  }

  //TODO fix mutable/immutable issue - only immutables on akka
  def setElementAt(x:Int, y:Int, e:Element) = elements(indexFor(x, y)) = e
  def setElementAt(e:Element) = elements(indexFor(e.x, e.y)) = e

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

  def getSurroundingElements(element: Element): Array[Element] = {
    val x = element.x
    val y = element.y

    val elements = getElements
    val result = new Array[Element](8)
    result(0) = elements(indexFor(x - 1, y - 1))
    result(1) = elements(indexFor(x, y - 1))
    result(2) = elements(indexFor(x + 1, y - 1))

    result(3) = elements(indexFor(x - 1, y))
    result(4) = elements(indexFor(x + 1, y))

    result(5) = elements(indexFor(x - 1, y + 1))
    result(6) = elements(indexFor(x, y + 1))
    result(7) = elements(indexFor(x + 1, y + 1))
    result
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
