package org.kirhgoff.ap.core

import java.awt.{Graphics, Graphics2D, Color}
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

import org.kirhgoff.ap.model.lifegame.{LifeGameElement, LifeGameWorldModel}

abstract class WorldPrinter {
  var counter = 0
  var path = "/tmp/images/"

  def printEndOfTheWorld() = {
    println ("============================>End of the world")
  }

  def renderElement(element: Element, chars: Array[Array[Char]]): Unit
  def renderElement(g:Graphics, element: Element, cellWidth:Double, cellHeight:Double): Unit

  def toAsciiSquare(world:WorldModel) = {
    val elements = world.getElements
    //println ("Elements:" + elements.size)
    val asci:Array[Array[Char]] = Array.ofDim[Char](world.width, world.height)
    elements.foreach(e => renderElement(e, asci))
    asci.map(row => row.toList.mkString("")).toList.mkString("\n")
  }

  def createPicture (world:WorldModel) = {
    val screenWidth = 500
    val screenHeight = 500
    val cellWidth = screenWidth.toDouble/world.width
    val cellHeight = screenHeight.toDouble/world.height
    val elements = world.getElements

    val canvas = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB)
    val g = canvas.createGraphics()
    g.setColor(Color.WHITE)
    g.fillRect(0, 0, canvas.getWidth, canvas.getHeight)

    g.setColor(Color.BLACK)
    elements.foreach(e => renderElement(g, e, cellWidth, cellHeight))

    g.dispose()
    ImageIO.write(canvas, "png", new java.io.File(f"${path}world${counter}%04d.png"))
    counter += 1
  }

}
