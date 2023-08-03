package actors

import akka.actor._

object ActorsIntro extends App {
  // Part 1 - Actor Systems
  // It is recommended to have only one actor system per application
  // The name of the actor system should be unique and only contain alphanumeric characters and
  // non-leading dashes '-' or underscores '_'
  val actorSystem = ActorSystem("firstActorSystem")
  println(actorSystem.name)

  // Part 2 - Create actors
  // Actors are uniquely identified
  // Messages are asynchronous
  // Each actor may respond differently
  // Actors are really encapsulated

  // Word count actor
  class WordCountActor extends Actor {
    // Internal data
    var totalWords = 0

    // Receive == PartialFunction[Any, Unit]
    // Behavior
    def receive: Receive = {
      case message: String =>
        println(s"[word counter] I have received: $message")
        totalWords += message.split(" ").length

      case msg => println(s"[word counter] I cannot understand ${msg.toString}")
    }
  }

  // Part 3 - Instantiate our actor

  // You can't instantiate an actor by calling new WordCountActor
  // You have to use the actor system
  // WordCountActor is a singleton
  val wordCounter: ActorRef        =
    actorSystem.actorOf(Props[WordCountActor], "wordCounter")
  val anotherWordCounter: ActorRef =
    actorSystem.actorOf(Props[WordCountActor], "anotherWordCounter")

  // Part 4 - Communicate!
  wordCounter ! "I am learning Akka and it's pretty damn cool!" // "tell"
  anotherWordCounter ! "A different message"

  // Asynchronous!
  // The message is sent to a queue and the actor picks it up when it can

  // How to instantiate an actor with constructor arguments
  class Person(name: String) extends Actor {
    override def receive: Receive = {
      case "hi" => println(s"Hi, my name is $name")
      case _    =>
    }
  }

  val person = actorSystem.actorOf(Props(new Person("Bob"))) // Discouraged

  // Encouraged
  object Person {
    def props(name: String) = Props(new Person(name))
  }

  val person2 = actorSystem.actorOf(Person.props("Alice"))

  person ! "hi"
  person2 ! "hi"

}
