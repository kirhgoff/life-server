package org.kirhgoff.ap.core

import akka.actor.{ActorSystem, ActorRef, Props, Actor}
import akka.routing.{RoundRobinPool, RoundRobinRouter}

import scala.collection.mutable

/**
 * Created by kirilllastovirya on 14/12/14.
 */

sealed trait RunnerMessage

case class CalculateNewState(elements:List[Element]) extends RunnerMessage
case class Work(element:Element) extends RunnerMessage
case class Result(newElement:Element) extends RunnerMessage
case class NewStateIsReady(elements:List[Element]) extends RunnerMessage

/**
 * Worker class - actor to calculate elements
 */
class Worker extends Actor {
  def receive = {
    case Work(element:Element) ⇒ {
      val newState = element.calculateNewState
      //println("Worker:" + element + "->" + newState)
      sender ! Result(newState)
    }
  }
}

/**
 * Master - splits a task to calculate world into separate tasks
 * @param nrOfWorkers - how many workers to use
 * @param listener - the employer
 */
class Master(nrOfWorkers: Int, listener: ActorRef)  extends Actor {

  val workerRouter = context.actorOf(Props[Worker].withRouter(RoundRobinPool(nrOfWorkers)), name = "workerRouter")
  var numberOfResults:Int = _
  var newElements:mutable.MutableList[Element] = mutable.MutableList()

  def receive = {
    //Make sure we are in correct state
    case CalculateNewState(_) if numberOfResults != 0 => throw new RuntimeException("Incorrect sequence of calls, check the code 0")
    case Result(_) if numberOfResults == 0 => throw new RuntimeException("Incorrect sequence of calls, check the code 1")

    //The logic itself
    case CalculateNewState(elements) => {
      //println("Calculating new state")
      numberOfResults = elements.length
      elements.map(workerRouter ! Work(_))
    }
    case Result(newElement) => {
      //println ("Result received:" + newElement)
      newElements += newElement
      numberOfResults -= 1
      if (numberOfResults == 0) {
        listener ! NewStateIsReady(newElements.toList)
        newElements = mutable.MutableList()        
      }
    }
  }
}

/**
 * Eployer. Created with a world to run and runs it
 * @param world - world to run
 * @param iterationCount - time till apocalypse
 */
class Listener(world:WorldModel, iterationCount:Int) extends Actor {
  var iterations:Int = 0
  var worldPrinter:WorldPrinter = new WorldPrinter (world, '0', '-')

  def receive = {
    case NewStateIsReady(elements) ⇒ {
      println("New state is ready:\n" + worldPrinter.print(elements))
      iterations += 1
      world.setElements(elements)
      if (iterations >= iterationCount) {
        worldPrinter.printEndOfTheWorld ()
        context.system.shutdown()
      } else {
        sender ! CalculateNewState(elements)
      }
    }
  }
}


object Main {
  def main (args: Array[String]) {
    val worldGenerator = new WorldGenerator (10, 10, 0.6)
    val world = worldGenerator.generate

    calculate(10, world, 20)
  }

  def calculate(nrOfWorkers: Int, world: WorldModel, iterations: Int) {
    val system = ActorSystem("Life-model-calculations")

    val listener = system.actorOf(Props(new Listener(world, iterations)), name = "listener")
    val master = system.actorOf(Props(new Master(nrOfWorkers, listener)), name = "master")

    println("Started with world:\n" + new WorldPrinter (world, '0', '-').print(world.getElements))
    master ! CalculateNewState(world.getElements)
  }
}
