package org.kirhgoff.ap.core

trait Element {
  def liveFurther(element: Element, position: Position):Element

  def isAlive: Boolean

  def setAlive(alive:Boolean)

  def dieStrategy: Strategy

  def position: Position

  def moveStrategy:Strategy

  def breedStrategy:Strategy

  def feedStrategy:Strategy

  def shouldDie: Boolean

  def canMove: Boolean

  def canFeed: Boolean

  def canBreed: Boolean

}
