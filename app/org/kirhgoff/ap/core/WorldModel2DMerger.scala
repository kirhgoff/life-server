package org.kirhgoff.ap.core

import scala.collection.mutable

/**
 * Created by Kirill Lastovirya (kirill.lastovirya@gmail.com) aka kirhgoff on 17/05/15.
 */
class WorldModel2DMerger(width:Int, height:Int, elements:List[Element], val elementMerger:ElementMerger)
  extends WorldModel2D(width, height) with WorldModelMerger
{
  var results = new mutable.MutableList[Element]() ++ elements
  def getResults: List[Element] = results.toList

  //TODO use world-specific element mergers
  def merge(newState:Element, created:List[Element], removed:List[Element]) = {
    //Update state
    results(indexFor(newState)) = newState

    removed.map{
      remove:Element => {
        val index: Int = indexFor(remove)
        val target = results(index)
        //TODO could require ability to return nothing
        results(index) = elementMerger.remove(target)
      }
    }

    created.map{
      create:Element => {
        val index: Int = indexFor(create)
        val target = results(index)
        results(index) = elementMerger.createdElement(target, create)
      }
    }
  }

  //TODO create PrintableWorld trait
  override def printer: WorldPrinter = throw new IllegalAccessException("Should never be called")
  override def merger = this
}
