package org.kirhgoff.ap.core

import org.kirhgoff.ap.core

/**
 * Created by kirilllastovirya on 14/12/14.
 */

case class WorldDimension (aroundSense:Array[Boolean])

class WorldModel(val width:Int, val height:Int) {
  var elements:List[Element] = List()

  /**
   *
   * Indices of surrounding cells
   * -----
   * -012-
   * -3X4-
   * -567-
   * -----
   *
   * @param x
   * @param y
   * @return
   */
  def giveEnvironmentFor(x: Int, y: Int):WorldDimension = {
    val result = new Array[Boolean] (8)
    //TODO make indices
//    val baseIndex = width * y + x
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


  def setElements(elements: List[Element]) = {this.elements = elements}

  def getElements:List[Element] = elements

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
}