package actors.lectures

import akka.actor._
import akka.routing._

object Routers extends App {

  /** Different ways to do it
    *
    *   - Manually -> Won't show it here
    *   - Programmatically
    */
  class Slave extends Actor with ActorLogging {

    def receive: Receive = { case msg => log.info(s"msg") }

  }

  // Programmatically
  val system = ActorSystem("Routers")
  val pool   = system.actorOf(RoundRobinPool(5).props(Props[Slave]))

  // From Configuration
  // Adding proper configuration to ActorSystem route
  val config = """
  akka {
    actor.deployment {
      config-example {
        router = round-robin-pool
        nr-of-instances = 5
      }
    }
  }
  """.stripMargin

  // val poolNrTwo = system.actorOf(FromConfig.props(Props[Slave]), "config-example") // -> SAME NAME

}
