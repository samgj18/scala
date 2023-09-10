package actors.exercises

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object ActorBehaviour extends App {

  // 1- Recreate the Counter Actor with context.become and NO MUTABLE STATE
  object Counter {
    case object Increment
    case object Decrement
    case object Print
  }

  class CounterActor(seed: Int) extends Actor {
    import Counter._

    override def receive: Receive = counter(seed)

    def counter(count: Int): Receive = {
      case Increment => context.become(counter(count + 1))
      case Decrement => context.become(counter(count - 1))
      case Print     => println(s"[counter] My current count is $count")
    }

  }

  object CounterActor {
    def props(seed: Int): Props = Props(new CounterActor(seed))
  }

  import Counter._

  val system       = ActorSystem("actorBehaviour")
  val counterActor = system.actorOf(CounterActor.props(0), "counterActor")

  (1 to 5).foreach(_ => counterActor ! Increment)
  (1 to 3).foreach(_ => counterActor ! Decrement)

  counterActor ! Print // [counter] My current count is 2

  // 2- Simplified voting system
  case class Vote(candidate: String)
  case object VoteStatusRequest
  case class VoteStatusReply(candidate: Option[String])

  class Citizen extends Actor {
    override def receive: Receive = vote(None)

    def vote(candidate: Option[String]): Receive = {
      case Vote(selection)   =>
        candidate match {
          case None            => context.become(vote(Some(selection)))
          case Some(candidate) =>
        }
      case VoteStatusRequest => sender() ! VoteStatusReply(candidate)
    }
  }

  case class AggregateVotes(citizens: Set[ActorRef])

  class VoteAggregator(votes: Map[String, Int]) extends Actor {
    override def receive: Receive = aggregator(votes)

    def aggregator(acc: Map[String, Int]): Receive = {
      case AggregateVotes(citizens)   =>
        citizens.foreach { _ ! VoteStatusRequest }
      case VoteStatusReply(candidate) =>
        candidate match {
          case None            =>
          case Some(candidate) =>
            val votes = acc.updated(candidate, acc.get(candidate).getOrElse(0) + 1)
            context.become(aggregator(votes))
            println(s"[votes] Current votes are $votes")
        }

    }

  }

  object VoteAggregator {
    def props(votes: Map[String, Int]) = Props(new VoteAggregator(votes))
  }

  val alice   = system.actorOf(Props[Citizen])
  val bob     = system.actorOf(Props[Citizen])
  val charlie = system.actorOf(Props[Citizen])

  alice ! Vote("Martin")
  bob ! Vote("Roland")
  charlie ! Vote("Roland")

  val voteAggregator = system.actorOf(VoteAggregator.props(Map.empty))
  voteAggregator ! AggregateVotes(Set(alice, bob, charlie))

  /** Print status of the votes
    *
    * Martin -> 1 Roland -> 2
    */

}
