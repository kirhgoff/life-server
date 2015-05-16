package org.kirhgoff.ap.core

trait Strategy {
  def apply(value: Element, environment: Environment):Unit

  def getCreatedElements: List[Element]
  def getRemovedElements: List[Element]
  def getNewState:Element
}

class DoNothingStrategy(var being:Element) extends Strategy {
  override def getNewState: Element = being
  override def getCreatedElements: List[Element] = List.empty
  override def getRemovedElements: List[Element] = List.empty
  override def apply(being: Element, environment: Environment) {}
}

object DoNothingStrategy {
  def apply(being:Element) = new DoNothingStrategy(being)
}
