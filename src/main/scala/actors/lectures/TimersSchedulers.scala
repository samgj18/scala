package actors.lectures

import akka.actor._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContextExecutor

object TimersSchedulers extends App {
  val system                                              = ActorSystem("TimersSchedulersDemo")
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  case object TimerKey
  case object Start
  case object Reminder
  case object Stop

  class SimpleActor extends Actor with ActorLogging {
    override def receive: Receive = {
      case Start    =>
        log.info("Starting...")
        context.system.scheduler.scheduleOnce(500.millis, self, Reminder)
      case Reminder =>
        log.info("I am reminded")
      case Stop     =>
        log.warning("Stopping...")
        context.stop(self)
    }
  }

  // val simpleActor = system.actorOf(Props[SimpleActor], "simpleActor")
  // simpleActor ! Start
  // simpleActor ! Stop
  //
  // val routine: Cancellable = system.scheduler.schedule(1.second, 2.seconds) {
  //   simpleActor ! Reminder
  // }
  //
  // system.scheduler.scheduleOnce(5.seconds) {
  //   routine.cancel()
  // }

  // Rules of thumb
  //
  // 1. Do not use unstable references in scheduled messages, i.e. actor that may be stopped while the scheduler is running
  // 2. All scheduled tasks run when the system is terminated regardless of the type of the scheduler
  // 3. Schedulers are not precise, do not expect them to be for long term tasks, i.e. months or years

  /** Exercise: implement a self-closing actor
    *
    *   - if the actor receives a message (anything), you have 1 second to send it another message
    *   - if the time window expires, the actor will stop itself
    *   - if you send another message, the time window is reset
    */

  class SelfClosingActor extends Actor with ActorLogging {
    def cancel: Cancellable = {
      context.system.scheduler.scheduleOnce(1.second) {
        self ! "stop"
      }
    }

    def receive: Receive = reset(cancel)

    def reset(window: Cancellable): Receive = {
      case "stop" => {
        log.info("Stopping")
        context.stop(self)
      }
      case msg    => {
        log.info(s"Message received: $msg")
        window.cancel()
        context.become(reset(cancel))
      }
    }
  }

  // val selfClosingActor = system.actorOf(Props[SelfClosingActor], "SelfClosingActor")
  // system.scheduler.scheduleOnce(750.millis) {
  //   selfClosingActor ! "ping"
  // }
  //
  // system.scheduler.scheduleOnce(1.2.second) {
  //   selfClosingActor ! "ping"
  // }
  //
  // system.scheduler.scheduleOnce(1.3.second) {
  //   selfClosingActor ! "ping"
  // }
  //
  // system.scheduler.scheduleOnce(2.second) {
  //   system.log.info("Sending Pong to SelfClosingActor")
  //   selfClosingActor ! "pong"
  // }
  //
  // // Straight to deadletters
  // system.scheduler.scheduleOnce(4.second) {
  //   system.log.info("Sending Pong to SelfClosingActor")
  //   selfClosingActor ! "pong"
  // }

  /** Timer
    */

  class TimerBasedHearBeatActor extends Actor with ActorLogging with Timers {
    timers.startSingleTimer(TimerKey, Start, 500.millis)

    def receive: Receive = {
      case Start    => {
        log.info("Starting...")
        timers.startTimerWithFixedDelay(TimerKey, Reminder, 1.second)
      }
      case Reminder =>
        log.info("I am reminded")
      case Stop     =>
        log.warning("Stopping...")
        timers.cancel(TimerKey)
        context.stop(self)
    }
  }

  val timerBasedHearBeatActor = system.actorOf(Props[TimerBasedHearBeatActor], "TimerBasedHearBeatActor")
  system.scheduler.scheduleOnce(5.seconds) {
    timerBasedHearBeatActor ! Stop
  }

}
