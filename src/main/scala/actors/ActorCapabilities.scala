package actors

import akka.actor._

object ActorCapabilities extends App {
  class SimpleActor extends Actor {

    // context.self === `this` in OOP

    override def receive: Receive = {
      case "Hi!" => context.sender() ! "Hello, there!" // replying to a message
      case message: String =>
        println(s"[${context.self.path}] I have received: $message")
      case number: Int =>
        println(s"[simple actor] I have received a NUMBER: $number")
      case SpecialMessage(contents) =>
        println(s"[simple actor] I have received something SPECIAL: $contents")
      case SendMessageToYourself(content) => self ! content
      case SayHiTo(ref) => ref ! "Hi!" // alice is being passed as the sender
      case WirelessPhoneMessage(content, ref) => ref forward (content + "s") // I keep the original sender of the WPM
    }

  }

  val system = ActorSystem("actorCapabilitiesDemo")
  val simpleActor = system.actorOf(Props[SimpleActor], "simpleActor")

  simpleActor ! "Hello, actor!"
  // Who is the sender of this message? -> deadLetters
  // How to read this: deadLetters is sending a message (telling) 42 to simpleActor
  simpleActor ! 42

  final case class SpecialMessage(contents: String)
  // How to read this: deadLetters is sending a message (telling) SpecialMessage("some special content") to simpleActor
  simpleActor ! SpecialMessage("some special content")

  // 1 - messages can be of any type
  // a) messages must be IMMUTABLE
  // b) messages must be SERIALIZABLE (This means that the JVM can transform the message into a byte stream and send it over the network)
  // in practice use case classes and case objects
  //
  // 2 - actors have information about their context and about themselves

  final case class SendMessageToYourself(content: String)
  simpleActor ! SendMessageToYourself("I am an actor and I am proud of it")

  // 3 - actors can REPLY to messages
  val alice = system.actorOf(Props[SimpleActor], "alice")
  val bob = system.actorOf(Props[SimpleActor], "bob")

  final case class SayHiTo(ref: ActorRef)
  // How to read this: alice is sending a SayHiTo message to bob
  // alice is the sender of the SayHiTo message
  // bob is the receiver of the SayHiTo message
  alice ! SayHiTo(bob)

  // 4 - dead letters
  // Dead letters is a special actor that receives messages that were not delivered
  alice ! "Hi!" // reply to "me"

  // 5 - forwarding messages
  case class WirelessPhoneMessage(content: String, ref: ActorRef)
  alice ! WirelessPhoneMessage("Hi", bob) // noSender
}
