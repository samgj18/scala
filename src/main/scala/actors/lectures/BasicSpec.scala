package actors.lectures

import akka.actor.{Actor, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpecLike

import scala.concurrent.duration._
import java.util.Random

class BasicSpec
    extends TestKit(ActorSystem("BasicSpec"))
    with ImplicitSender  // used for send reply scenarios `testActor`
    with AnyWordSpecLike // natural like written tests
    with BeforeAndAfterAll { // allows to define hooks for before and after

  import BasicSpec._

  override protected def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  // Suite
  "the thing being tested" should {
    // Test
    "do this" in {
      // Testing Scenario
    }
  }

  "A simple actor should" should {
    "send back the same message" in {
      val echo    = system.actorOf(Props[SimpleActor])
      val message = "test"

      echo ! message

      expectMsg(message) // akka.test.single-expect-default
    }
  }

  // failing test
  "A blackhole actor" should {
    "send back some message" in {
      val actor   = system.actorOf(Props[Blackhole])
      val message = "test"

      actor ! message

      expectNoMessage(1.second)
    }
  }

  // Message Assertion
  "A LabTestActor" should {
    val actor = system.actorOf(Props[LabTestActor])
    "turn a message into uppercase" in {
      val message = "I love akka"
      val reply   = expectMsgType[String]

      assert(reply == "I LOVE AKKA")
    }

    "reply to a greeting" in {
      actor ! "greeting"
      expectMsgAnyOf("hi", "hello")
    }

    "reply with tech" in {
      actor ! "tect"
      expectMsgAllOf("Scala", "Akka")
    }

    "reply with tech variation" in {
      actor ! "tect"

      val messages = receiveN(2) // Seq[Any]
    }

    "reply with tech variation number two" in {
      actor ! "tech"
      expectMsgPF() {
        case "Scala" => // Only care hat the PF is defined
        case "Akka"  =>
      }
    }
  }
}

object BasicSpec {
  // Here must be everything that will be used by the tests
  class SimpleActor extends Actor {
    override def receive: Receive = { case message =>
      sender() ! message
    }
  }

  class Blackhole extends Actor {
    override def receive: Receive = Actor.emptyBehavior
  }

  class LabTestActor extends Actor {
    val random                    = new Random()
    override def receive: Receive = {
      case "greeting" => sender() ! (if (random.nextBoolean()) "hi" else "hello")
      case "tech"     =>
        sender() ! "Scala"
        sender() ! "Akka"
      case message    =>
        sender() ! message.toString.toUpperCase

    }
  }
}
