package actors.lectures

import akka.actor.{Actor, ActorRef, ActorSystem, Props, ActorLogging}
import akka.testkit.{ImplicitSender, TestKit, TestProbe, EventFilter}
import com.typesafe.config.ConfigFactory
import java.util.Random
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpecLike
import scala.concurrent.duration._

class InterceptLogSpec
    extends TestKit(ActorSystem("TestProbeSpec", ConfigFactory.load().getConfig("interception")))
    with ImplicitSender
    with AnyWordSpecLike
    with BeforeAndAfterAll {

  import InterceptLogSpec._

  override protected def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  import InterceptLogSpec._

  "An intercepter" should {
    val message = "A message"
    "get all messages from logs" in {
      // To be able to query the logger this needs to be configured
      // on the application.conf adding the following
      //
      // interception { <- this 'interception is arbitrary name'
      //  akka {
      //    loggers = ["akka.testkit.TestEventListener"]
      //  }
      // }
      //
      // And configure the actor system with this with ConfigFactory
      //
      // We can increase the timeout of intercept by changing the following config:
      // akka.test.filter-leeway
      EventFilter.info(pattern = message) intercept {
        val logger = system.actorOf(Props[LoggerActor])

        logger ! message
      }
    }

    "get exceptions as well" in {
      EventFilter[RuntimeException](occurrences = 1) intercept {
        val panic = system.actorOf(Props[PanicActor])

        panic ! "whatever"
      }
    }
  }
}

object InterceptLogSpec {
  class LoggerActor extends Actor with ActorLogging {
    def receive: Receive = { case "message" =>
      log.info("message")
    }
  }

  class PanicActor extends Actor {
    def receive: Receive = throw new RuntimeException("I panicked")
  }
}
