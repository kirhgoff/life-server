package akka

import akka.actor._
import akka.event.Logging
import akka.routing.RoundRobinPool

//TODO rename to bigsoup
import org.kirhgoff.ap.core._

sealed trait RunnerMessage

case class StartWorldProcessing(world:WorldModel, listener:WorldModelListener, iterations:Int) extends RunnerMessage
case class CalculateNewState(world:WorldModel) extends RunnerMessage
case class ProcessElement(element:Element, environment:Environment) extends RunnerMessage
case class ElementUpdated(newState:Element, created:List[Element], removed:List[Element]) extends RunnerMessage
case class WorldUpdated(elements:List[Element]) extends RunnerMessage
case class InterruptWork() extends RunnerMessage

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
class BigSoupOperatorActor(val workers: Int) extends Actor {
  val log = Logging(context.system, this)

  var iterations:Int = 0
  var currentIteration:Int = -1
  var world:WorldModel = null
  var listener:WorldModelListener = null

  var manager = createManager

  def createManager: ActorRef = {
    context.actorOf(Props(new ElementBatchProcessorActor(workers)), name = "master")
  }

  def receive = {
    case StartWorldProcessing(world:WorldModel, listener:WorldModelListener, iterations:Int) => {
      log.info ("received StartWorldProcessing")
      if (alreadyRunning) {
        log.info("already running, ignoring command")
      } else {
        //manager = createManager

        this.world = world
        this.listener = listener
        this.iterations = iterations
        this.currentIteration = 0

        listener.worldUpdated(world)

        manager ! CalculateNewState(world)
      }
    }
    case WorldUpdated(elements) ⇒ {
      log.info (s"received WorldUpdated $currentIteration")
      world.setElements(elements)

      listener.worldUpdated(world)

      currentIteration += 1
      if (currentIteration >= iterations) {
        iterations = 0
        currentIteration = -1

        //Stop system
        log.info("final iteration, stopping manager")
        context.stop(manager)
      } else {
        sender ! CalculateNewState(world)
      }
    }
    case InterruptWork => {
      log.info("received InterruptWork, stopping manager")
      //Stop system
      context.stop(manager)
    }
  }

  def alreadyRunning: Boolean = {
    this.iterations != 0 || this.currentIteration != -1
  }
}


object LifeActors {
  val workers = 100

  val system  = ActorSystem(s"BigSoupAkka")
  val operator = system.actorOf(
    Props(new BigSoupOperatorActor(workers)), //TODO check this
    name = "listener"
  )

  def run (world:WorldModel, listener: WorldModelListener, iterations:Int) {
    operator ! StartWorldProcessing(world, listener, iterations)
  }

  //TODO create sync method

  def stop = {
    operator
  }
}
