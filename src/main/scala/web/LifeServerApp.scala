package web

import com.typesafe.scalalogging.slf4j.{Logger, Logging}

import akka.actor.ActorSystem
import org.slf4j.LoggerFactory
import spray.routing.SimpleRoutingApp
//import akka.event.Logging


/**
 * Created by Kirill Lastovirya (kirill.lastovirya@gmail.com) aka kirhgoff on 05/10/15.
 */
object LifeServerApp extends App with SimpleRoutingApp with Logging {


  implicit val system = ActorSystem("my-system")

  val log = Logger(LoggerFactory.getLogger(this.getClass))


  startServer(interface = "localhost", port = 8080) {
      pathSingleSlash {
        log.info("single slash!")

        //      path("hello") {
//        get {
//          complete {
//            <h1>Say hello to spray</h1>
//          }
//        }
        //getFromBrowseableDirectories()

        getFromFile("web/index.html")
      }
      path("index.html") {
        log.info("index.html")
        getFromFile("web/index.html")
      }
      path("hello") {
        log.info("hello")
        complete {
          <h1>Say hello to spray</h1>
        }
      }

    }
  }
