package org.kirhgoff.ap.model.lotke_volterra

import java.awt.Graphics

import org.kirhgoff.ap.core.{EmptyElement, Element, WorldPrinter}
import org.kirhgoff.ap.model.lifegame.LifeGameElement

class LotkeVolterraWorldPrinter(val preySymbol:Char, val hunterSymbol:Char, emptySymbol:Char) extends WorldPrinter {
  def renderElement(g:Graphics, element:Element, cellWidth:Double, cellHeight:Double) = {
    element match {
      case e: LifeGameElement =>  if (element.isAlive) {
        g.fillRect((e.x*cellWidth).toInt, (e.y*cellHeight).toInt, cellWidth.toInt, cellHeight.toInt)
        //TODO draw symbol or change color for kinds
      }

      case _ => throw new IllegalArgumentException("Incorrect element class")
    }
  }

  def renderElement(element:Element, asci: Array[Array[Char]]) = {
    asci(element.y)(element.x) = element match {
      case e: Being =>  e.kind match {
        case Prey => preySymbol
        case Hunter => hunterSymbol
      }
      case e:EmptyElement => emptySymbol
      case _ => throw new IllegalArgumentException("Incorrect element class")
    }
  }

}
