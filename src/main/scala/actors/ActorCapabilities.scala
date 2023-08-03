package actors

import akka.actor._

object ActorCapabilities extends App {
  class SimpleActor extends Actor {

    // context.self === `this` in OOP

    override def receive: Receive = {
      case "Hi!"                              => context.sender() ! "Hello, there!" // replying to a message
      case message: String                    =>
        println(s"[${context.self.path}] I have received: $message")
      case number: Int                        =>
        println(s"[simple actor] I have received a NUMBER: $number")
      case SpecialMessage(contents)           =>
        println(s"[simple actor] I have received something SPECIAL: $contents")
      case SendMessageToYourself(content)     => self ! content
      case SayHiTo(ref)                       => ref ! "Hi!"                        // alice is being passed as the sender
      case WirelessPhoneMessage(content, ref) =>
        ref forward (content + "s") // I keep the original sender of the WPM
    }

  }

  val system      = ActorSystem("actorCapabilitiesDemo")
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
  val bob   = system.actorOf(Props[SimpleActor], "bob")

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

object ActorExercises extends App {

  /** Exercises
    *
    *   1. a Counter actor
    *      - Increment
    *      - Decrement
    *      - Print
    *
    * 2. a Bank account as an actor
    *
    * Receives:
    *   - Deposit an amount
    *   - Withdraw an amount
    *   - Statement
    *
    * Replies:
    *   - Success
    *   - Failure
    *
    * Interact with some other kind of actor
    */

  // Domain of the counter
  object Counter {
    case object Increment
    case object Decrement
    case object Print
  }

  class CounterActor extends Actor {

    var seed: Int = 0

    import Counter._

    override def receive: Receive = {
      case Increment => seed += 1
      case Decrement => seed -= 1
      case Print     => println(s"[counter] My current count is $seed")
    }

  }

  val system  = ActorSystem("actorExercises")
  val counter = system.actorOf(Props[CounterActor], "counter")

  import Counter._

  (1 to 5).foreach(_ => counter ! Increment)
  (1 to 3).foreach(_ => counter ! Decrement)
  counter ! Print // should print 2

  // Bank account
  // Domain of the bank account
  object BankAccount {
    case class Deposit(amount: Int)
    case class Withdraw(amount: Int)
    case object Statement
    case class TransactionSuccess(message: String)
    case class TransactionFailure(message: String)
  }

  class BankAccount extends Actor {

    import BankAccount._

    var balance: Int = 0

    override def receive: Receive = {
      case Deposit(amount) =>
        if (amount < 0) sender() ! TransactionFailure("Invalid deposit amount")
        else {
          balance += amount
          sender() ! TransactionSuccess(s"Successfully deposited $amount")
        }

      case Withdraw(amount) =>
        if (amount < 0) sender() ! TransactionFailure("Invalid withdraw amount")
        else if (amount > balance) sender() ! TransactionFailure("Insufficient funds")
        else {
          balance -= amount
          sender() ! TransactionSuccess(s"Successfully withdrew $amount")
        }

      case Statement => sender() ! s"Your balance is $balance"
    }

  }

  object Person {
    case class Communicate(account: ActorRef)
  }

  class Person extends Actor {
    import Person._
    import BankAccount._

    override def receive: Receive = {
      case Communicate(account) =>
        account ! Deposit(10000)
        account ! Withdraw(90000)
        account ! Withdraw(500)
        account ! Statement
      case message              => println(s"[person] Unsupported message: $message")
    }
  }

  import BankAccount._
  import Person._

  val bankAccount = system.actorOf(Props[BankAccount], "bankAccount")
  val person      = system.actorOf(Props[Person], "billionaire")

  person ! Communicate(bankAccount)

}
