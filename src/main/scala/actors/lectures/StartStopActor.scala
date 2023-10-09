package actors.lectures

import akka.actor._

object StartStopActor extends App {

  val system = ActorSystem("startStopActorDemo")

  // Starting Actor
  // val first = system.actorOf(StartStopActor1.props, "first")

  object Parent {
    case class StartChild(name: String)
    case class StopChild(name: String)
    case object Stop
  }

  class Parent extends Actor with ActorLogging {
    def receive: Receive = children(Map.empty)

    def children(reference: Map[String, ActorRef] = Map.empty): Receive = {
      case Parent.StartChild(name) => {
        log.info(s"Starting child $name")
        val child = context.actorOf(Props[Child], name)
        context.become(children(reference.updated(name, child)))
      }

      case Parent.StopChild(name) => {
        log.info(s"Stopping child $name")
        val child: Option[ActorRef] = reference.get(name)
        // This is a non-blocking call (asynchronous)
        child.foreach(childRef => context.stop(childRef))
      }

      case Parent.Stop => {
        log.info("Stopping myself")
        // This is a non-blocking call (asynchronous)
        // This will stop all the children first and then the parent
        context.stop(self)
      }

      case message => log.info(message.toString)
    }
  }

  class Child extends Actor with ActorLogging {
    def receive: Receive = { case message =>
      log.info(s"Received message: $message")
    }
  }

  import Parent._

  val parent = system.actorOf(Props[Parent], "parent")
  parent ! StartChild("child1")
  val child  = system.actorSelection("/user/parent/child1")
  child ! "hi kid!"

  parent ! StopChild("child1")

  // Some of these messages will still be received by the child actor
  // for (_ <- 1 to 50) child ! "are you still there?"

  parent ! StartChild("child2")
  val child2 = system.actorSelection("/user/parent/child2")
  child2 ! "hi, second child"

  parent ! Stop

  for (_ <- 1 to 10) parent ! "parent, are you still there?"
  for (i <- 1 to 100) child2 ! s"[$i] second kid, are you still alive?"

  val looseActor = system.actorOf(Props[Child])
  looseActor ! "hello, loose actor"
  // This is a blocking call (synchronous)
  looseActor ! PoisonPill
  looseActor ! "loose actor, are you still there?"

  val abruptlyTerminatedActor = system.actorOf(Props[Child])
  abruptlyTerminatedActor ! "you are about to be terminated"
  // This is a blocking call (synchronous)
  abruptlyTerminatedActor ! Kill
  abruptlyTerminatedActor ! "you have been terminated"

  /** Death watch
    */
  class Watcher extends Actor with ActorLogging {
    import Parent._

    def receive: Receive = {
      case StartChild(name) => {
        log.info(s"Starting and watching child $name")
        val child = context.actorOf(Props[Child], name)
        // This is a non-blocking call (asynchronous)
        // When the child dies, the parent will receive a Terminated message
        context.watch(child)
      }

      case Terminated(ref) => {
        log.info(s"The reference that I'm watching $ref has been stopped")
      }
    }
  }

  val watcher      = system.actorOf(Props[Watcher], "watcher")
  watcher ! StartChild("watchedChild")
  val watchedChild = system.actorSelection("/user/watcher/watchedChild")
  Thread.sleep(500)
}
