package actors.lectures

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.event.Logging

object ActorLoggingAsync extends App {
  class SimpleActorWithExplicitLogger extends Actor {
    val logger = Logging(context.system, this)

    override def receive: Receive = { case message => logger.info(message.toString) }
  }

  val system = ActorSystem("logging-demo")
  val actor  = system.actorOf(Props[SimpleActorWithExplicitLogger])

  actor ! "Logging a simple message"

  class ActorWithLogging extends Actor with ActorLogging {
    override def receive: Receive = { case message => log.info(message.toString) }
  }

  val actor2 = system.actorOf(Props[ActorWithLogging])

  actor2 ! "Logging a simple message"
}
