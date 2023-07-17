package actors

import akka.actor._

object ActorCapabilities extends App {
  class SimpleActor extends Actor {

    override def receive: Receive = {
      // case "Hi!" => context.sender() ! "Hello, there!" // replying to a message
      case message: String => println(s"[${context.self.path}] I have received: $message")
      case number: Int => println(s"[simple actor] I have received a NUMBER: $number")
      // case SpecialMessage(contents) => println(s"[simple actor] I have received something SPECIAL: $contents")
      // case SendMessageToYourself(content) => self ! content
      // case SayHiTo(ref) => ref ! "Hi!" // alice is being passed as the sender
      // case WirelessPhoneMessage(content, ref) => ref forward (content + "s") // I keep the original sender of the WPM
    }

  }

  val system = ActorSystem("actorCapabilitiesDemo")
  val simpleActor = system.actorOf(Props[SimpleActor], "simpleActor")

  simpleActor ! "Hello, actor!"
  simpleActor ! 42
}
