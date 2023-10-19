package actors.lectures

import akka.actor._
import scala.io.Source
import akka.pattern._
import scala.concurrent.duration._

object BackoffSupervision extends App {

  case object ReadFile
  class FileBasedPersistentActor extends Actor with ActorLogging {
    var dataSource: Option[Source] = None

    override def preStart(): Unit = log.info("Persistent actor starting")

    override def postStop(): Unit = log.warning("Persistent actor has stopped")

    override def preRestart(reason: Throwable, message: Option[Any]): Unit = log.warning("Persistent actor restarting")

    override def receive: Receive = { case ReadFile =>
      if (dataSource.isEmpty) {
        dataSource = Some(
          Source.fromFile("src/main/resources/testfiles/important.txt")
        ) // will throw FileNotFoundException
        log.info("I've just read some IMPORTANT data: " + dataSource.get.getLines().toList)
      }
    }
  }

  val system      = ActorSystem("BackoffSupervisorDemo")
  val simpleActor = system.actorOf(Props[FileBasedPersistentActor], "simpleActor")
  simpleActor ! ReadFile

  val simpleSupervisorProps = BackoffSupervisor.props(
    BackoffOpts.onFailure(
      Props[FileBasedPersistentActor],
      "simpleBackoffActor",
      3.seconds, // then 6s, 12s, 24s
      30.seconds,
      0.2
    )
  )

  /** simpleSupervisor
    *   - child called simpleBackoffActor (props of type FileBasedPersistentActor)
    *   - supervision strategy is the default one (restarting on everything)
    *     - first attempt after 3 seconds
    *     - next attempt is 2x the previous attempt (exponential backoff)
    */
  val simpleBackoffSupervisor = system.actorOf(simpleSupervisorProps, "simpleSupervisor")
  simpleBackoffSupervisor ! ReadFile

  val stopSupervisorProps = BackoffSupervisor.props(
    BackoffOpts
      .onStop(
        Props[FileBasedPersistentActor],
        "stopBackoffActor",
        3.seconds, // then 6s, 12s, 24s
        30.seconds,
        0.2
      )
      .withSupervisorStrategy(
        OneForOneStrategy() { case _ =>
          SupervisorStrategy.Stop
        }
      )
  )

  val stopSupervisor = system.actorOf(stopSupervisorProps, "stopSupervisor")
  stopSupervisor ! ReadFile

  class EagerFBPActor extends FileBasedPersistentActor {
    override def preStart(): Unit = {
      log.info("Eager actor starting")
      dataSource = Some(
        Source.fromFile("src/main/resources/testfiles/important.txt")
      ) // will throw FileNotFoundException
      log.info("I've just read some IMPORTANT data: " + dataSource.get.getLines().toList)
    }
  }

  val repeatedSupervisorProps = BackoffSupervisor.props(
    BackoffOpts.onStop(
      Props[EagerFBPActor],
      "eagerActor",
      1.seconds, // then 2s, 4s, 8s, 16s
      30.seconds,
      0.1
    )
  )

  val repeatedSupervisor = system.actorOf(repeatedSupervisorProps, "eagerSupervisor")
  // repeatedSupervisor ! ReadFile
  /** eagerSupervisor
    *   - child eagerActor
    *   - will die on start with FileNotFoundException
    *   - trigger the supervision strategy in eagerSupervisor => STOP eagerActor
    */

}
