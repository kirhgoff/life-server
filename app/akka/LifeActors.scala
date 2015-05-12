package akka

import org.kirhgoff.ap.model.lifegame._
import play.api.libs.json._
import akka.actor._
import akka.routing.{RoundRobinPool, RoundRobinRouter}

import scala.collection.mutable
import controllers.Application
import models.World

import org.kirhgoff.ap.core._

sealed trait RunnerMessage

case class InitWorld(world:WorldModel, iterations:Int) extends RunnerMessage
case class CalculateNewState(elements:List[Element]) extends RunnerMessage
case class ProcessElement(element:Element) extends RunnerMessage
case class ElementUpdated(newElement:Element) extends RunnerMessage
case class WorldUpdated(elements:List[Element]) extends RunnerMessage

/**
 * Worker class - actor to calculate elements
 */
class Worker extends Actor {
  def receive = {
    case ProcessElement(element:LifeGameElement) ⇒ {
      val newState = element.calculateNewState()
      //println("Worker:" + element + "->" + newState)
      sender ! ElementUpdated(newState)
    }
  }
}

/**
 * Master - splits a task to calculate world into separate tasks
 * @param nrOfWorkers - how many workers to use
 */
class AggregatingMaster(nrOfWorkers: Int)  extends Actor {
  val workerRouter = context.actorOf(Props[Worker].withRouter(RoundRobinPool(nrOfWorkers)), name = "workerRouter")
  var numberOfResults:Int = _
  var newElements:mutable.MutableList[Element] = mutable.MutableList()
  var operator:ActorRef = null

  def receive = {
    //Make sure we are in correct state
    case CalculateNewState(_) if numberOfResults != 0 => throw new RuntimeException("Incorrect sequence of calls, check the code 0")
    case ElementUpdated(_) if numberOfResults == 0 => throw new RuntimeException("Incorrect sequence of calls, check the code 1")

    //The logic itself
    case CalculateNewState(elements) => {
      //println("CalculateNewState")
      numberOfResults = elements.length
      operator = sender
      elements.map(workerRouter ! ProcessElement(_))
    }
    case ElementUpdated(newElement) => {
      //println ("Result received:" + newElement)
      newElements += newElement
      numberOfResults -= 1
      if (numberOfResults == 0) {
        operator ! WorldUpdated(newElements.toList)
        newElements = mutable.MutableList()        
      }
    }
  }
}

/**
 * Created with a world to run and runs it
 */
class CalculatingOperator(val workers: Int) extends Actor {
  var iterations:Int = 0
  var currentIteration:Int = 0
  var world:LifeGameWorldModel = null
  val master = LifeActors.system.actorOf(Props(new AggregatingMaster(workers)), name = "master")

  def receive = {
    case WorldUpdated(elements) ⇒ {
      //println ("operator.WorldUpdated")
      val worldPrinter:WorldPrinter = world.printer
      world.setElements(elements)
      val stringWorld: String = worldPrinter.toAsciiSquare(world)
      //println(s"-------------\n$stringWorld")
      Application.lifeChannel.push(Json.toJson(World(stringWorld)))

      currentIteration += 1
      if (currentIteration >= iterations) {
        worldPrinter.printEndOfTheWorld ()
        iterations = 0
        currentIteration = 0
      } else {
        Thread.sleep(100)
        sender ! CalculateNewState(elements)
      }
    }
    case InitWorld(world:LifeGameWorldModel, iterations:Int) => {
      //println ("operator.InitWord")
      this.world = world
      val worldPrinter:WorldPrinter = world.printer

      this.iterations = iterations
      this.currentIteration = 0

      Application.lifeChannel.push(Json.toJson(World(worldPrinter.toAsciiSquare(world))))
      master ! CalculateNewState(world.getElements)
    }
  }
}


object LifeActors {
  val workers = 100
  val system = ActorSystem("life-model-calculations")
  val operator = system.actorOf(Props(new CalculatingOperator(workers)), name = "listener")

  def run (width:Integer, height:Integer, world:WorldModel, iterations:Int) {
    operator ! InitWorld(world, iterations)
  }

  def stop = {} //TODO
}
