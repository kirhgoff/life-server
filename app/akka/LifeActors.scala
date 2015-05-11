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

case class InitWorld(world:LifeGameWorldModel, iterations:Int) extends RunnerMessage
case class CalculateNewState(elements:List[LifeGameElement]) extends RunnerMessage
case class ProcessElement(element:LifeGameElement) extends RunnerMessage
case class ElementUpdated(newElement:LifeGameElement) extends RunnerMessage
case class WorldUpdated(elements:List[LifeGameElement]) extends RunnerMessage

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
  var newElements:mutable.MutableList[LifeGameElement] = mutable.MutableList()
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
  var worldPrinter:WorldPrinter = new WorldPrinter ('*', ' ')
  var world:LifeGameWorldModel = null
  val master = LifeActors.system.actorOf(Props(new AggregatingMaster(workers)), name = "master")

  def receive = {
    case WorldUpdated(elements) ⇒ {
      //println ("operator.WorldUpdated")
      world.setElements(elements)
      val stringWorld: String = worldPrinter.print(world)
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
      this.iterations = iterations
      this.currentIteration = 0

      Application.lifeChannel.push(Json.toJson(World(worldPrinter.print(world))))
      master ! CalculateNewState(world.getElements)
    }
  }
}


object LifeActors {
  val workers = 100
  val lifeRatio = 0.6

  val system = ActorSystem("life-model-calculations")
  val operator = system.actorOf(Props(new CalculatingOperator(workers)), name = "listener")

  def run (width:Integer, height:Integer, iterations:Int) {

    val world: LifeGameWorldModel = WorldGenerator.generate(width, height)
    LifeGenerator.applyLife(lifeRatio, world)
    println("Started with world:\n" + new WorldPrinter ('0', '-').print(world))

    operator ! InitWorld(world, iterations)
  }

  def stop = {} //TODO
}
