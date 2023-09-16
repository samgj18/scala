package actors.exercises

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import scala.collection.immutable.Queue

object WordCount extends App {
  // Distributed Word Counting

  class WordCounterMaster extends Actor {
    import WordCounterMaster._

    override def receive: Receive = create(Queue.empty, Map.empty, 0)

    def create(queue: Queue[ActorRef], tracker: Map[Int, ActorRef], task: Int): Receive = {
      case Initialize(n) =>
        assert(n > 0)
        context.become(
          create(
            Queue.from((0 until n).map { id =>
              val name = s"word-count-worker-$id"
              context.actorOf(Props[WordCounterWorker], name)
            }),
            Map.empty,
            0
          )
        )

      case text: String => {
        queue.dequeueOption match {
          case None                     => self ! text
          case Some((worker, newQueue)) => {
            val newId      = task + 1
            worker ! WordCountTask(newId, text)
            val newTracker = tracker.updated(newId, sender())
            context.become(create(newQueue, newTracker, newId))
          }
        }
      }

      case WordCountReply(id, count, ref) => {
        println(s"[master] Reply received with id $id and count $count")
        tracker.get(id).foreach(_ ! count)
        val newTracker = tracker.removed(id)
        context.become(create(queue.enqueue(ref), newTracker, id))
      }
    }
  }

  object WordCounterMaster {

    /** Creates n children of type WordCounterWorker
      */
    case class Initialize(n: Int)
    case class WordCountTask(id: Int, text: String)
    case class WordCountReply(id: Int, count: Int, ref: ActorRef)
  }

  class WordCounterWorker extends Actor {
    import WordCounterMaster._

    override def receive: Receive = { case WordCountTask(id, text) =>
      println(s"Task received with id $id and text $text")
      sender() ! WordCounterMaster.WordCountReply(id, text.split(" ").length, self)
    }
  }

  /*
   * Create WordCounterMaster
   * Send Initialize to WordCounterMaster
   * Send "Akka is awesome" to WordCounterMaster
   * WordCounterMaster will send a WordCountTask to one of
   * the children
   * WordCounterWorker replies with a WordCountReploy
   * WordCounterMaster replies with 3 to the sender
   *
   * R -> WCM -> WCW
   *  R <- WCM <-
   *
   * Round Robin Logic
   */

  class TestActor extends Actor {
    import TestActor._
    import WordCounterMaster._

    def receive: Receive = {
      case Go => {
        val wordCounterActor = system.actorOf(Props[WordCounterMaster], "word-count-master")
        wordCounterActor ! Initialize(5)
        val texts            = List(
          "Very cool",
          "Very very cool",
          "Very very very cool"
        )

        texts.foreach(wordCounterActor ! _)
      }

      case c: Int => println(s"Response from ${sender()} received $c")
    }
  }

  object TestActor {
    case object Go
  }

  import TestActor._

  val system = ActorSystem("distributedWordCounter")
  val actor  = system.actorOf(Props[TestActor])

  actor ! Go
}
