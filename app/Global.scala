import akka.LifeActors
import play.api.GlobalSettings

object Global extends GlobalSettings {

  override def onStart(application: play.api.Application) {
    //LifeActors
  }
  
  override def onStop(application: play.api.Application) { 
    //LifeActors.system.shutdown()
  }
}
