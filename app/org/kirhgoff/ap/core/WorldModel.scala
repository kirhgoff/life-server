package org.kirhgoff.ap.core

trait WorldModel {
  def getEnvironmentFor(element: Element): Environment

  def process(being:Element):Unit

  def collectChanges(elementsToCreate: List[Element], elementsToRemove: List[Element]) = ???
  def mergeChanges():Unit
}
