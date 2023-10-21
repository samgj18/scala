package actors.lectures

import akka.actor._

object Stash extends App {
  case object Open
  case object Close
  case object Read
  case class Write(data: String)

  // ResourceActor is Open
  // - It can receive read/write requests to the resource
  // - Otherwise it'll postpone all read/write requests until state is open
  // - Read/Write messages are postponed

  // ResourceActor is Closed by default
  //  - When it receives the Open it'll switch to Open
  //  - When is open it can read/write
  //  - Can receive Close message => switch to Closed state

  // Step 1: Mixin Stash trait
  class ResourceActor extends Actor with ActorLogging with Stash {
    private var inner: String = ""

    def receive: Receive = closed

    def closed: Receive = {
      case Open => {
        log.info("Opening Resource")
        // Step 3: Unstash when you switch message handler
        unstashAll()
        context.become(open)
      }
      case msg  => {
        log.info(s"Stashing ${msg.toString}")
        // Step 2: Stash away what you can't handle
        stash()
      }
    }

    def open: Receive = {
      case Read =>
        log.info(s"$inner")

      case Write(data) => inner = data

      case Close => {
        unstashAll()
        context.become(closed)
      }

      case msg => {
        log.info(s"Stashing ${msg.toString}")
        // Step 2: Stash away what you can't handle
        stash()
      }
    }
  }

  val system   = ActorSystem("StashDemo")
  val resource = system.actorOf(Props[ResourceActor])

  resource ! Read // Stashed
  resource ! Open // Switch to Open
  resource ! Open // Stashed
  resource ! Write("This is read") // Handled
  resource ! Close // Switch to Close; Switch to Open
  resource ! Read // Handled
}
