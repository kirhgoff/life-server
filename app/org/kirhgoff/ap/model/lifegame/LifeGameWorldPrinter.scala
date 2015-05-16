package org.kirhgoff.ap.model.lifegame

import java.awt.Graphics

import org.kirhgoff.ap.core.{WorldPrinter, Element}


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
