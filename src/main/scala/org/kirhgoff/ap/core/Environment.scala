package org.kirhgoff.ap.core

//Provides access to surrounding elements with positions
trait Environment {

}

case class CloseSurroundings (surroundings:Array[Boolean]) extends Environment
case class ElementSurroundings (around:Array[Element]) extends Environment




