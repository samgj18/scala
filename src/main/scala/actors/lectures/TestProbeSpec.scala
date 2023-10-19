package actors.lectures

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpecLike

class TestProbeSpec
    extends TestKit(ActorSystem("TestProbeSpec"))
    // The ImplicitSender trait allows us to use the self reference
    with ImplicitSender
    with AnyWordSpecLike
    with BeforeAndAfterAll {

  override protected def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  import TestProbeSpec._

  "A master actor" should {
    "register a slave" in {
      val master = system.actorOf(Props[Master])
      val slave  = TestProbe("slave")

      master ! Register(slave.ref)

      expectMsg(Ack)
    }

    "send the work to the slave actor" in {
      val master = system.actorOf(Props[Master])
      val slave  = TestProbe("slave")
      master ! Register(slave.ref)
      expectMsg(Ack)

      val workload = "I love Akka"

      master ! Work(workload)

      slave.expectMsg(SlaveWork(workload, testActor))
      // you can also mock the replies
      slave.reply(Completed(3, testActor))

      // Check receiveWhile
      expectMsg(Report(3))
    }
  }
}

object TestProbeSpec {
  case class Register(slave: ActorRef)
  case class Work(text: String)
  case class SlaveWork(text: String, requester: ActorRef)
  case class Completed(count: Int, requester: ActorRef)
  case class Report(count: Int)
  case object Ack

  class Master extends Actor {
    override def receive: Receive = {
      case Register(slave) =>
        sender() ! Ack
        context.become(online(slave, 0))
      case _               =>
    }

    def online(ref: ActorRef, count: Int): Receive = {
      case Work(text) =>
        ref ! SlaveWork(text, sender())

      case Completed(total, requester) =>
        val newCount = count + total
        requester ! Report(newCount)
        context.become(online(ref, newCount))
    }
  }
}
