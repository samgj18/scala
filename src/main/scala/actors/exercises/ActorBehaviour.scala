package actors.exercises

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object ActorBehaviour extends App {

  object Counter {
    case object Increment
    case object Decrement
    case object Print
  }

  class CounterActor extends Actor {
    import Counter._

    override def receive: Receive = counter(0)

    def counter(count: Int): Receive = {
      case Increment => context.become(counter(count + 1))
      case Decrement => context.become(counter(count - 1))
      case Print     => println(s"[counter] My current count is $count")
    }

  }

  import Counter._

  val system       = ActorSystem("actorBehaviour")
  val counterActor = system.actorOf(Props[CounterActor], "counterActor")

  (1 to 5).foreach(_ => counterActor ! Increment)
  // (1 to 3).foreach(_ => counterActor ! Decrement)

  counterActor ! Print
}
