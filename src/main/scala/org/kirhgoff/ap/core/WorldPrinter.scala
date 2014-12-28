package org.kirhgoff.ap.core

class WorldPrinter(val aliveSymbol:Char, val deadSymbol:Char) {

  def printEndOfTheWorld() = {
    println ("============================>End of the world")
  }

  def print(world:WorldModel) = {
    val elements = world.getElements
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
