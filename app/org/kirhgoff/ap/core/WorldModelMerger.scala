package org.kirhgoff.ap.core

import scala.collection.mutable

/**
 * Created by Kirill Lastovirya (kirill.lastovirya@gmail.com) aka kirhgoff on 17/05/15.
 */
class WorldModelMerger(width:Int, height:Int, elements:List[Element]) extends WorldModel2D(width, height)  {
  var results = new mutable.MutableList[Element]() ++ elements
  def getResults: List[Element] = results.toList


  def merge(newState:Element, created:List[Element], removed:List[Element]) = {
    //Update state
    results(indexFor(newState)) = newState

    removed.map{
      e:Element => results(indexFor(newState)) = new EmptyElement(e.x, e.y)
    }

    created.map{
      e:Element => results(indexFor(newState)) = e
    }
  }

  //TODO why this method is here?
  override def printer: WorldPrinter = throw new IllegalAccessException("Should never be called")
}
