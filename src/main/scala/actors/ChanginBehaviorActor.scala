package actors

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props

object ChangingBehaviorActor extends App {

  class FussyKid extends Actor {
    import FussyKid._
    import Mom._

    var state = HAPPY

    override def receive: Receive = {
      case Food(VEGETABLE) => state = SAD
      case Food(CHOCOLATE) => state = HAPPY
      case Ask(message)    =>
        if (state == HAPPY) sender() ! KidAccept
        else sender() ! KidReject
    }
  }

  class StatelessFussyKid extends Actor {
    import FussyKid._
    import Mom._

    override def receive: Receive = happyReceive

    def happyReceive: Receive = {
      case Food(VEGETABLE) => context.become(sadReceive) // Swaps `receive` with passed handler
      case Food(CHOCOLATE) =>                            // Stay happy
      case Ask(_)          => sender() ! KidAccept
    }

    def sadReceive: Receive = {
      case Food(VEGETABLE) => // Stay sad
      case Food(CHOCOLATE) =>
        context.become(happyReceive) // If adding false then it becomes a stack where we can change
      // behavior with content.unbecome
      case Ask(_) => sender() ! KidReject
    }
  }

  object FussyKid {
    case object KidAccept
    case object KidReject

    val HAPPY = "happy"
    val SAD   = "sad"
  }

  class Mom extends Actor {
    import Mom._
    import FussyKid._

    override def receive: Receive = {
      case MomStart(kidRef) =>
        kidRef ! Food(VEGETABLE)
        kidRef ! Ask("Do you want to play?")
      case KidReject        => println("You can't eat what you want")
      case KidAccept        => println("You're a good boy")
    }
  }

  object Mom {
    case class MomStart(kidRef: ActorRef)
    case class Food(food: String)
    case class Ask(message: String)

    val VEGETABLE = "veggies"
    val CHOCOLATE = "chocolate"
  }

  val system            = ActorSystem("changingBehaviorActor")
  val fussyKid          = system.actorOf(Props[FussyKid])
  val statelessFussyKid = system.actorOf(Props[StatelessFussyKid])
  val mom               = system.actorOf(Props[Mom])

  // mom ! Mom.MomStart(fussyKid)
  mom ! Mom.MomStart(statelessFussyKid)
}
