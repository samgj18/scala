package actors.lectures

import akka.actor._
import com.typesafe.config.ConfigFactory
import java.util.Random
import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.Future

object Dispatchers extends App {
  class Counter extends Actor with ActorLogging {

    def receive: Receive = count(0)

    def count(i: Int): Receive = {
      case msg => {
        log.info(s"[$i]: ${msg.toString}")
        context.become(count(i + 1))
      }
    }
  }
  // Method #1 - programmatic/in code

  val system = ActorSystem("DispatchersDemo")

  val actors = (1 to 10).map(i => system.actorOf(Props[Counter].withDispatcher("r-dispatcher"), s"counter_$i")).toList

  val random = new Random()

  // (1 to 1000).foreach(i => actors(random.nextInt(10)) ! i)

  // Method #2 - From Config. Name of the dispatcher is the same as the name of the config file
  val rDispatcher = system.actorOf(Props[Counter], "r-dispatcher")
  
  // (1 to 1000).foreach(i => rDispatcher ! i)

  // Dispatchers implement the ExecutionContext trait
  class DBActor extends Actor with ActorLogging {
    implicit val ec: ExecutionContextExecutor = context.dispatcher

    override def receive: Receive = { case msg =>
      Future {
        // wait on a resource
        Thread.sleep(5000)
        log.info(s"Success: $msg")
      }
    }
  }

  val dbActor = system.actorOf(Props[DBActor])
  dbActor ! "The meaning of life is 42"
}
