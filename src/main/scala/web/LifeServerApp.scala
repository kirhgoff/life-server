package web

import akka.actor.ActorSystem
import spray.routing.SimpleRoutingApp


/**
 * Created by Kirill Lastovirya (kirill.lastovirya@gmail.com) aka kirhgoff on 05/10/15.
 */
object LifeServerApp extends App with SimpleRoutingApp {
    implicit val system = ActorSystem("my-system")

    startServer(interface = "localhost", port = 8080) {
      path("hello") {
        get {
          complete {
            <h1>Say hello to spray</h1>
          }
        }
      }
    }
  }
