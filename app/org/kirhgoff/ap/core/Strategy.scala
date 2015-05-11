package org.kirhgoff.ap.core

trait Strategy {
  def apply(value: Element, environment: Environment):Unit

  def createdElements: List[Element]
  def removedElements: List[Element]
  def newPosition: Position
  def newState:Element
}

class ZeroStrategy(var being:Element) extends Strategy {
  override def newPosition: Position = being.position
  override def newState: Element = being
  override def createdElements: List[Element] = List.empty
  override def removedElements: List[Element] = List.empty
  override def apply(being: Element, environment: Environment) {}
}

object ZeroStrategy {
  def apply(being:Element) = new ZeroStrategy(being)
}
