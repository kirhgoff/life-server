package org.kirhgoff.ap.model.lotke_volterra

import org.kirhgoff.ap.core.{Environment, Element, Strategy}

class PreyStrategy extends Strategy {
  override def apply(value: Element, environment: Environment): Unit = ???

  override def getRemovedElements: List[Element] = ???

  override def getNewState: Element = ???

  override def getCreatedElements: List[Element] = ???
}
