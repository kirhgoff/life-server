package org.kirhgoff.ap.core

<<<<<<< HEAD
import java.awt.Color
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

class WorldPrinter(val world:WorldModel, val aliveSymbol:Char, val deadSymbol:Char) {
  var counter = 0
  var path = "c:/Tmp/images/"

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

  def createPicture (elements:List[Element]) = {
    val screenWidth = 500
    val screenHeight = 500
    val cellWidth = screenWidth/world.width
    val cellHeight = screenHeight/world.height

    val canvas = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB)
    val g = canvas.createGraphics()
    g.setColor(Color.WHITE)
    g.fillRect(0, 0, canvas.getWidth, canvas.getHeight)

    g.setColor(Color.BLACK)
    elements.map(
      element => {
        if (element.isAlive) g.fillRect(element.x*cellWidth, element.y*cellHeight, cellWidth, cellHeight)
      }
    )

    g.dispose()
    ImageIO.write(canvas, "png", new java.io.File(path + "world" + ))

  }

}
