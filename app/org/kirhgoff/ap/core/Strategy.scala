package org.kirhgoff.ap.core

trait Strategy {
  def apply(value: Element, environment: Environment):Unit

  def getCreatedElements: List[Element]
  def getRemovedElements: List[Element]
  def getNewState:Element
}

class ZeroStrategy(var being:Element) extends Strategy {
  override def getNewState: Element = being
  override def getCreatedElements: List[Element] = List.empty
  override def getRemovedElements: List[Element] = List.empty
  override def apply(being: Element, environment: Environment) {}
}

object ZeroStrategy {
  def apply(being:Element) = new ZeroStrategy(being)
}
