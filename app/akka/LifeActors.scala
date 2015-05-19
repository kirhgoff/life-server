package akka

import java.util.ConcurrentModificationException

import akka.actor._
import akka.routing.RoundRobinPool
import controllers.Application
import models.World
import org.kirhgoff.ap.core._
import play.api.libs.json._

sealed trait RunnerMessage

case class StartWorldProcessing(world:WorldModel, listener:WorldModelListener, iterations:Int) extends RunnerMessage
case class CalculateNewState(world:WorldModel) extends RunnerMessage
case class ProcessElement(element:Element, environment:Environment) extends RunnerMessage
case class ElementUpdated(newState:Element, created:List[Element], removed:List[Element]) extends RunnerMessage
case class WorldUpdated(elements:List[Element]) extends RunnerMessage
//TODO updated

/**
 * Worker class - actor to calculate elements
 */
class ElementProcessorActor extends Actor {
  def receive = {
    case ProcessElement(element:Element, environment:Environment) ⇒ {
      val strategy:Strategy = element.getStrategy(environment)
      strategy.apply(element, environment)
      //println("Worker:" + element + "->" + newState)
      sender ! ElementUpdated(
        strategy.getNewState,
        strategy.getCreatedElements,
        strategy.getRemovedElements
      )
    }
  }
}

/**
 * Master - splits a task to calculate world into separate tasks
 * @param nrOfWorkers - how many workers to use
 */
class ElementBatchProcessorActor(nrOfWorkers: Int)  extends Actor {
  val workerRouter = context.actorOf(Props[ElementProcessorActor].withRouter(RoundRobinPool(nrOfWorkers)), name = "workerRouter")
  var numberOfResults:Int = _
//  var newElements:mutable.MutableList[Element] = mutable.MutableList()
  var operator:ActorRef = null
  var worldMerger:WorldModelMerger = null

  def receive = {
    //Make sure we are in correct state
    case CalculateNewState if numberOfResults != 0 => throw new RuntimeException("Incorrect sequence of calls, check the code 0")
    case ElementUpdated if numberOfResults == 0 => throw new RuntimeException("Incorrect sequence of calls, check the code 1")

    case CalculateNewState(world) => {
      //println("CalculateNewState")
      operator = sender
      worldMerger = world.makeMerger

      val elements:List[Element] = world.getElements.filter(!_.isInstanceOf[EmptyElement])

      numberOfResults = elements.length
      elements.map {
        e:Element => workerRouter ! ProcessElement(e, world.getEnvironmentFor(e))
      }
    }
    case ElementUpdated(newState, created, deleted) => {
      //println ("Result received:" + newElement)
      worldMerger.merge(newState, created, deleted)
      numberOfResults -= 1
      if (numberOfResults == 0) {
        operator ! WorldUpdated(worldMerger.getResults)
      }
    }
  }
}

/**
 * Created with a world to run and runs it
 */
class PlayWorldRunnerActor(val workers: Int) extends Actor {
  var iterations:Int = 0
  var currentIteration:Int = -1
  var world:WorldModel = null
  var listener:WorldModelListener = null

  val master = LifeActors.system.actorOf(Props(new ElementBatchProcessorActor(workers)), name = "master")

  def receive = {
    case StartWorldProcessing(world:WorldModel, listener:WorldModelListener, iterations:Int) => {
      //println ("operator.InitWord")
      if (alreadyRunning) throw new ConcurrentModificationException("Should not happen")
      this.world = world
      this.listener = listener
      this.iterations = iterations
      this.currentIteration = 0

      listener.worldUpdated(world)

      master ! CalculateNewState(world)
    }
    case WorldUpdated(elements) ⇒ {
      //println ("operator.WorldUpdated")
      world.setElements(elements)

      listener.worldUpdated(world)

      currentIteration += 1
      if (currentIteration >= iterations) {
        //TODO remove?
        world.printer.printEndOfTheWorld ()
        iterations = 0
        currentIteration = -1
      } else {
        Thread.sleep(100)
        sender ! CalculateNewState(world)
      }
    }
  }

  def alreadyRunning: Boolean = {
    this.iterations != 0 || this.currentIteration != -1
  }
}


object LifeActors {
  val workers = 100
  val system = ActorSystem("life-model-calculations")
  val operator = system.actorOf(Props(new PlayWorldRunnerActor(workers)), name = "listener")

  def run (world:WorldModel, listener: WorldModelListener, iterations:Int) {
    operator ! StartWorldProcessing(world, listener, iterations)
  }

  def stop = {} //TODO
}
