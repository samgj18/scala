package actors.lectures

import akka.testkit._
import akka.actor._
// First Step to Ask Pattern
import akka.pattern._
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatest.BeforeAndAfterAll
import scala.concurrent.duration._
import akka.util.Timeout
import scala.concurrent.ExecutionContext
import scala.util.Success
import scala.util.Failure

class AskSpec extends TestKit(ActorSystem("AskSpec")) with ImplicitSender with AnyWordSpecLike with BeforeAndAfterAll {
  override protected def afterAll(): Unit = TestKit.shutdownActorSystem(system)

  import AskSpec._

  "An authenticator" should {
    "fail to authenticate a non-registered user" in {
      val manager = system.actorOf(Props[AuthManager])
      manager ! Authenticate("Samuel", "H")
      expectMsg(AuthFailure(Some("Password not found")))
    }
  }
}

object AskSpec {

  case class Read(key: String)
  case class Write(key: String, value: String)

  class KVActor extends Actor with ActorLogging {
    def receive: Receive = online(Map.empty)

    def online(kv: Map[String, String]): Receive = {
      case Read(key) => {
        log.info(s"Reading the value at $key")
        sender() ! kv.get(key)
      }

      case Write(key, value) => {
        log.info(s"Writing the value at $key with $value")
        context.become(online(kv.updated(key, value)))
      }
    }
  }

  case class RegisterUser(user: String, pass: String)
  case class Authenticate(user: String, pass: String)

  case class AuthFailure(msg: Option[String])
  case object AuthSuccess

  class AuthManager extends Actor with ActorLogging {
    protected val authDb = context.actorOf(Props[KVActor])

    // Second Step Logistics
    implicit val timeout: Timeout     = Timeout(1.second)
    implicit val ec: ExecutionContext = context.dispatcher

    def receive: Receive = {
      case RegisterUser(u, p) => authDb ! Write(u, p)
      case Authenticate(u, p) => authenticate(u, p)
    }

    def authenticate(u: String, p: String): Unit = {

      // Step Three (Ask)
      val future         = authDb ? Read(u)
      val originalSender = sender()

      // Step Four Handle the future
      future.onComplete {
        // Step Five: Never call methods on the actor instance or access mutable state in onComplete
        // i.e. using sender() instead of originalSender
        case Success(None)           => originalSender ! AuthFailure(Some("Password not found"))
        case Success(Some(password)) => {
          if (p == password) originalSender ! AuthSuccess
          else originalSender ! AuthFailure(Some("Incorrect password"))
        }
        case Failure(exception)      => originalSender ! AuthFailure(Some("${extends.getMessage}"))
        case Success(value)          => originalSender ! AuthFailure(Some("${value}"))

      }
    }
  }

  class PipedAuthManager extends AuthManager {

    override def authenticate(u: String, p: String): Unit = {
      val future = authDb ? Read(u)             // Future[Any]
      val option = future.mapTo[Option[String]] // Future Option[String]

      val response = option.map {
        case Some(password) =>
          if (p == password) AuthSuccess
          else AuthFailure(Some("Incorrect password"))
        case None           => AuthFailure(Some("Password not found"))
      }

      /**
        * When the future completes send the message to the `given` actor ref
        */
      response.pipeTo(sender())
    }

  }
}
