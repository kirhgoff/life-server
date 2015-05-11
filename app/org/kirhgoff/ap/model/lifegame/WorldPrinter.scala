package org.kirhgoff.ap.model.lifegame

import java.awt.Color
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

class WorldPrinter(val aliveSymbol:Char, val deadSymbol:Char) {
  var counter = 0
  var path = "/tmp/images/"

  def printEndOfTheWorld() = {
    println ("============================>End of the world")
  }

  def print(world:WorldModel) = {
    val elements = world.getElements
    //println ("Elements:" + elements.size)
    val asci:Array[Array[Char]] = Array.ofDim[Char](world.width, world.height)
    elements.map{
      e:Element => {
        asci(e.y)(e.x) = if (e.isAlive) aliveSymbol else deadSymbol
      }
    }
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
    elements.map(
      element => {
        if (element.isAlive) g.fillRect((element.x*cellWidth).toInt, (element.y*cellHeight).toInt, cellWidth.toInt, cellHeight.toInt)
      }
    )

    g.dispose()
    ImageIO.write(canvas, "png", new java.io.File(f"${path}world${counter}%04d.png"))
    counter += 1
  }



}
