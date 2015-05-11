package org.kirhgoff.ap.model.lotke_volterra

import org.kirhgoff.ap.core._


abstract class LotkeVolterraWorldModel extends WorldModel {

  def process(being:Element) = {
    val environment:Environment = getEnvironmentFor(being)
    val strategy:Strategy =
      if (being.canFeed) being.feedStrategy
      else if (being.canBreed) being.breedStrategy
      else if (being.canMove) being.moveStrategy
      else if (being.shouldDie) being.dieStrategy
      else ZeroStrategy(being)

    strategy.apply(being, environment)
    val elementsToCreate:List[Element] = strategy.createdElements
    val elementsToRemove:List[Element] = strategy.removedElements
    val newPosition = strategy.newPosition
    val newState = strategy.newState

    //Merging is important
    // Will predator kill newborn prey? Or opposite?
    // Will moved prey kill newborn prey
    // do we see aging as new prey killing yonger prey (age + 1)?
    if (newState.isAlive) collectChanges(being.liveFurther(newState, newPosition) :: elementsToCreate, elementsToRemove)
    else  collectChanges(elementsToCreate, being :: elementsToRemove)
  }


}

object LotkeVolterraWorldModel {
  //Constants
  val InitialPreyEnergy = 1
  val InitialPredatorEnergy = 10
  val PreyMaturityAge = 2
  val PredatorMaturityAge = 2
}
