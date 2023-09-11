package actors.lectures

import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef
import akka.actor.ActorSystem

object ChildActor extends App {

  // Actors can create other actors

  class Parent extends Actor {
    import Parent._

    override def receive: Receive = create(None)

    def create(child: Option[ActorRef]): Receive = {
      case CreateChild(name) => {
        println(s"${self.path}: Creating Child")
        child match {
          case None        => {
            val ref = context.actorOf(Props[Child], name)
            context.become(create(Some(ref)))
          }
          case Some(value) =>
        }
      }

      case TellChild(message) =>
        child match {
          case None        =>
          case Some(child) => child forward message
        }

    }

  }

  object Parent {
    case class CreateChild(name: String)
    case class TellChild(message: String)
  }

  class Child extends Actor {

    override def receive: Receive = { case message =>
      println(s"${self.path}: I got - $message")
    }

  }

  val system = ActorSystem("ParentChildDemo")
  val parent = system.actorOf(Props[Parent], "parent")

  parent ! Parent.CreateChild("child")
  parent ! Parent.TellChild("I'm your father")

  // Actor Hierarchies
  // Parent -> Child -> GrandChild
  //        -> Child2 ->

  // Guardian Actors (Top-Level) - Owns the parent
  //
  // - /system = system guardian (manages logging, death watch, etc)
  // - /user = user-level guardian (manages all actors we create)
  // - / = the root guardian (manages system and user guardian)

  // Actor Selection
  val selection = Option(system.actorSelection("/user/parent/child"))
  selection.foreach(_ ! "I found you!")

  val nonExistingChild = Option(system.actorSelection("/user/parent/child2"))
  nonExistingChild.foreach(_ ! "I didn't find you, so I'm sending this to dead letters")

  // Danger!
  //
  // NEVER PASS MUTABLE ACTOR STATE, OR THE `THIS` REFERENCE, TO CHILD ACTORS

}
