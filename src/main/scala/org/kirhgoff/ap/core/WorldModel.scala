package org.kirhgoff.ap.core

import org.kirhgoff.ap.core

/**
 * Created by kirilllastovirya on 14/12/14.
 */

case class WorldDimension (aroundSense:Array[Boolean])

class WorldModel(val width:Int, val height:Int) {
  var elements:List[Element] = null

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
    val baseIndex = width * y + x
    result(0) = elements(baseIndex - x - 1).isAlive
    result(1) = elements(baseIndex - x).isAlive
    result(2) = elements(baseIndex - x + 1).isAlive
    result(3) = elements(baseIndex - 1).isAlive
    result(4) = elements(baseIndex + 1).isAlive
    result(5) = elements(baseIndex + x - 1).isAlive
    result(6) = elements(baseIndex + x).isAlive
    result(7) = elements(baseIndex + x + 1).isAlive

    WorldDimension(result)
  }


  def setElements(elements: List[Element]) = {this.elements = elements}

  def getElements:List[Element] = elements
}
