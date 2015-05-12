package org.kirhgoff.ap.core

trait WorldModel {
  def setElements(elements: List[Element])

  //TODO create separate class WorldGeometry?
  def height: Int
  def width: Int

  def getElements:List[Element]

  def getEnvironmentFor(element: Element): Environment

  def process(being:Element):Unit

  def collectChanges(elementsToCreate: List[Element], elementsToRemove: List[Element]) = ???
  def mergeChanges():Unit
}
