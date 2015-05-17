package org.kirhgoff.ap.core

/**
 * Created by Kirill Lastovirya (kirill.lastovirya@gmail.com) aka kirhgoff on 17/05/15.
 */
trait WorldModelMerger {
  def getResults: List[Element]

  def merge(newState:Element, created:List[Element], removed:List[Element])
}
