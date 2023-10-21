package actors.lectures

import akka.actor._
import akka.dispatch._
import com.typesafe.config.{Config, ConfigFactory}

object Mailboxes extends App {
  val system = ActorSystem("MailboxesDemo", ConfigFactory.load().getConfig("mailboxes"))

  class SimpleActor extends Actor with ActorLogging {
    override def receive: Receive = { case message =>
      log.info(s"[${self.path}] I have received $message")
    }
  }

  // 1. Setting up a custom mailbox
  /** Custom Priority Mailbox At Runtime, Akka will use reflection to instantiate the mailbox hence the need for the
    * config parameter
    *
    * P0 -> most important P1 -> ... P2 -> ...
    */
  class SupportTicketPriorityMailbox(settings: ActorSystem.Settings, config: Config)
      extends UnboundedStablePriorityMailbox(
        PriorityGenerator {
          case message: String if message.startsWith("[P0]") => 0
          case message: String if message.startsWith("[P1]") => 1
          case message: String if message.startsWith("[P2]") => 2
          case message: String if message.startsWith("[P3]") => 3
          case _                                             => 4
        }
      )

  // 2. Make it known in the config
  // 3. Attach the dispatcher to an actor
  val supportTicketLogger = system.actorOf(Props[SimpleActor].withDispatcher("support-ticket-dispatcher"))

  // supportTicketLogger ! PoisonPill
  //
  // supportTicketLogger ! "[P3] this thing would be nice to have"
  // supportTicketLogger ! "[P0] this needs to be solved NOW!"
  // supportTicketLogger ! "[P1] do this when you have the time"

  // After which time can I send another message and be prioritized accordingly?

  // Control-aware mailbox
  // Some messages are more important than others

  // Step 1 - mark important messages as control messages
  case object ManagementTicket extends ControlMessage

  // Step 2 - configure who gets the mailbox
  //  - Make the actor attach to the mailbox -> For this particular instance, not needed in general to do this
  val controlAwareActor = system.actorOf(Props[SimpleActor].withMailbox("control-mailbox"))

  // controlAwareActor ! "[P0] this needs to be solved NOW!"
  // controlAwareActor ! "[P1] do this when you have the time"
  // controlAwareActor ! ManagementTicket
  // controlAwareActor ! "[P2] this needs to be solved NOW!"
  // controlAwareActor ! "[P3] do this when you have the time"

  // Method #2 - using deployment config
  val altControlAwareActor = system.actorOf(Props[SimpleActor], "support-ticket-actor")

  altControlAwareActor ! "[P0] this needs to be solved NOW!"
  altControlAwareActor ! "[P1] do this when you have the time"
  altControlAwareActor ! ManagementTicket
  altControlAwareActor ! "[P2] this needs to be solved NOW!"
  altControlAwareActor ! "[P3] do this when you have the time"

}
