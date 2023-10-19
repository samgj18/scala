package actors.lectures

import akka.actor._
import akka.testkit._
import org.scalatest.wordspec._
import org.scalatest.BeforeAndAfterAll

class SupervisionSpec
    extends TestKit(ActorSystem("test-system"))
    with AnyWordSpecLike
    with ImplicitSender
    with BeforeAndAfterAll {

  override protected def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  import SupervisionSpec._

  "A supervisor" should {
    "resume its child in case of a minor fault" in {
      val supervisor = system.actorOf(Props[Supervisor], "supervisor")
      supervisor ! Props[FuzzyWordCounter]

      val child = expectMsgType[ActorRef]

      child ! "I love Akka"
      child ! Report
      expectMsg(3)

      child ! "Akka is awesome because I am learning to think in a whole new way"
      child ! Report
      expectMsg(3)
    }

    "restart its child in case of an empty sentence" in {
      val supervisor = system.actorOf(Props[Supervisor], "supervisor")
      supervisor ! Props[FuzzyWordCounter]

      val child = expectMsgType[ActorRef]

      child ! "I love Akka"
      child ! Report
      expectMsg(3)

      child ! ""
      child ! Report
      expectMsg(0)
    }

    "terminate its child in case of a major error" in {
      val supervisor = system.actorOf(Props[Supervisor], "supervisor")
      supervisor ! Props[FuzzyWordCounter]

      val child = expectMsgType[ActorRef]

      // Register a death watch on the child
      watch(child)
      child ! "akka is nice"
      val terminatedMessage = expectMsgType[Terminated]
      assert(terminatedMessage.actor == child)
    }

    "escalate an error when it does not know what to do" in {
      val supervisor = system.actorOf(Props[Supervisor], "supervisor")
      supervisor ! Props[FuzzyWordCounter]

      val child = expectMsgType[ActorRef]

      // Register a death watch on the child
      watch(child)
      child ! 43
      val terminatedMessage = expectMsgType[Terminated]
      assert(terminatedMessage.actor == child)
    }
  }
}

object SupervisionSpec {

  case object Report

  class Supervisor extends Actor {

    override val supervisorStrategy: SupervisorStrategy =
      // There are different strategies, this one is the simplest one
      // AllForOneStrategy() -> All the children are subject to the same strategy
      OneForOneStrategy() {
        // On Restart the actor instance is killed and a new one is created
        case _: NullPointerException     => SupervisorStrategy.Restart  // Restart the actor, reset the state
        case _: IllegalArgumentException => SupervisorStrategy.Stop     // Stop the actor
        case _: RuntimeException         => SupervisorStrategy.Resume   // Keep the accumulated state
        case _: Exception                => SupervisorStrategy.Escalate // Escalate to the parent
      }

    def receive: Receive = { case props: Props =>
      val childRef = context.actorOf(props)
      sender() ! childRef
    }
  }

  class FuzzyWordCounter extends Actor {
    var words            = 0
    def receive: Receive = {

      case ""               => throw new NullPointerException("empty string")
      case sentence: String =>
        if (sentence.length > 20) throw new RuntimeException("too long")
        else if (!Character.isUpperCase(sentence(0)))
          throw new IllegalArgumentException("sentence must start with uppercase")
        else words += sentence.split(" ").length
      case Report           => sender() ! words
      case _                => throw new Exception("can only receive strings or Report")
    }
  }
}
