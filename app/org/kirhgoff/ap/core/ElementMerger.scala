package org.kirhgoff.ap.core

/**
 * Created by Kirill Lastovirya (kirill.lastovirya@gmail.com) aka kirhgoff on 17/05/15.
 */
trait ElementMerger {
  def remove(element: Element): Element = new EmptyElement(element.x, element.y)

  def createdElement(target:Element, update:Element):Element
}

class SimpleMerger extends ElementMerger{
  override def createdElement(target: Element, update: Element): Element = update
}
