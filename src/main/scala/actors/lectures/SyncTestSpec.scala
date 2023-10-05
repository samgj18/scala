package actors.lectures

import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatest.BeforeAndAfterAll
import akka.testkit.TestKit
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.testkit.TestActorRef
import akka.actor.Props
import akka.testkit.CallingThreadDispatcher
import akka.testkit.TestProbe

class SyncTestSpec extends AnyWordSpecLike with BeforeAndAfterAll {
  implicit val system: ActorSystem = ActorSystem("SyncTestSpec")

  override protected def afterAll(): Unit = system.terminate()

  import SyncTestSpec._

  "A counter" should {
    "synchronously increase its counter" in {
      val counter = TestActorRef[Counter](Props[Counter])

      counter ! SyncTestSpec.Inc // I can be completely sure that this
      // has already reach the actor because for TestActorrRef messages
      // are sent in the calling thread.
    }

    "work on the calling thread dispatcher" in {
      // Whatever message I sent to this actor will happen on the calling thread
      val counter = system.actorOf(Props[Counter].withDispatcher(CallingThreadDispatcher.Id))
      val probe   = TestProbe()

      probe.send(counter, Read) // Here the probe already received the count because the caller is
      // the probe and every interaction happens in its thread
      probe expectMsg 0
    }
  }
}

object SyncTestSpec {
  case object Inc
  case object Dec
  case object Read

  class Counter extends Actor {

    def receive: Receive = counter(0)

    def counter(state: Int): Receive = {
      case Inc  => context.become(counter(state + 1))
      case Dec  => context.become(counter(state - 1))
      case Read => sender() ! state
    }
  }
}
