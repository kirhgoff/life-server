package org.kirhgoff.ap.core

/**
 * Created by kirilllastovirya on 14/12/14.
 */
class WorldPrinter(val world:WorldModel, val aliveSymbol:Char, val deadSymbol:Char) {

  def printEndOfTheWorld() = {
    println ("============================>End of the world")
  }

  def print(elements: List[Element]) = {
    //println ("Elements:" + elements.size)
    val asci:Array[Array[Char]] = Array.ofDim[Char](world.width, world.height)
    elements.map{
      e:Element => {
        asci(e.x)(e.y) = if (e.isAlive) aliveSymbol else deadSymbol
      }
    }
    asci.map(row => row.toList.mkString("")).toList.mkString("\n")
  }

}
