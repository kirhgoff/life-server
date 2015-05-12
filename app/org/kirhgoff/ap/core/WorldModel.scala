package org.kirhgoff.ap.core

trait WorldModel {

  //TODO create separate class WorldGeometry?
  def height: Int
  def width: Int

  def getEnvironmentFor(element: Element): Environment
  def getElements:List[Element]
  def setElements(elements: List[Element])

  def process(being:Element):Unit
  def collectChanges(elementsToCreate: List[Element], elementsToRemove: List[Element]):Unit
  def mergeChanges():Unit

  def printer:WorldPrinter
}
