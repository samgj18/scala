package actors.lectures

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpecLike

import scala.concurrent.duration._
import java.util.Random

class TimeAssertionSpec
    extends TestKit(ActorSystem("TestProbeSpec"))
    with ImplicitSender
    with AnyWordSpecLike
    with BeforeAndAfterAll {

  import TimeAssertionSpec._

  override protected def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A worker actor" should {
    val worker = system.actorOf(Props[WorkerActor])
    "reply with the meaning of life in a timely manner" in {
      within(500.millis, 1.second) { // Between 500 millis and at most 1 second
        worker ! Work
        expectMsg(WorkResult(42))
      }
    }

    "reply in a reasonable cadence" in {
      within(1.second) {
        worker ! WorkSeq
        val res = receiveWhile[Int](2.seconds, 500.millis, 10) { case WorkResult(res) =>
          res
        }

        assert(res.sum > 5)
      }
    }

    "reply to a test probe in a timely manner" in {
      within(1.second) {
        val probe = TestProbe()
        probe.send(worker, Work)
        probe.expectMsg(WorkResult(42))
      }
    }
  }

  object TimeAssertionSpec {

    case object Work
    case object WorkSeq
    case class WorkResult(res: Int)

    class WorkerActor extends Actor {
      override def receive: Receive = {
        case Work    =>
          // Long Computation
          Thread.sleep(500)
          sender() ! WorkResult(42)
        case WorkSeq =>
          val random = new Random()
          (0 until 10).map { _ =>
            Thread.sleep(random.nextInt(50))
            sender() ! WorkResult(1)
          }
      }
    }
  }
}
