package actors.lectures

import akka.actor._

object ActorLifecycle extends App {
  object StartChild

  class LifecycleActor extends Actor with ActorLogging {
    // By default, this method does nothing
    override def preStart(): Unit = log.info("I am starting")
    // By default, this method does nothing
    override def postStop(): Unit = log.info("I have stopped")

    def receive: Receive = { case StartChild =>
      context.actorOf(Props[LifecycleActor], "child")
    }
  }

  val system = ActorSystem("LifecycleDemo")
  val parent = system.actorOf(Props[LifecycleActor], "parent")
  parent ! StartChild
  parent ! PoisonPill

  /**
   * Restart
   */

  object Fail
  object FailChild
  object CheckChild
  object Check

  class Parent extends Actor with ActorLogging {
    private val child = context.actorOf(Props[Child], "supervisedChild")

    def receive: Receive = {
      case FailChild => child ! Fail
      case CheckChild => child ! Check 
    }
  }

  class Child extends Actor with ActorLogging {
    override def preStart(): Unit = log.info("Supervised child started")
    override def postStop(): Unit = log.info("Supervised child stopped")

    override def preRestart(reason: Throwable, message: Option[Any]): Unit =
      log.info(s"Supervised actor restarting because of ${reason.getMessage}")
    override def postRestart(reason: Throwable): Unit =
      log.info(s"Supervised actor restarted")

    def receive: Receive = {
      case Fail => {
        log.warning("Child will fail now")
        // This will throw an exception and the actor will be restarted
        throw new RuntimeException("I failed")
      }
      case Check => {
        log.info("Alive and kicking")
      }
    }

    val supervisor = system.actorOf(Props[Parent], "supervisor")
    supervisor ! FailChild

    // This is the default supervision strategy -> restart

  }

}
