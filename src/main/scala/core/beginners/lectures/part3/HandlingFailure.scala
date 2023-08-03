package lectures.part3

import scala.util.{Failure, Random, Success, Try}

object HandlingFailure extends App {
  val aSuccess = Success(4)
  val aFailure = Failure(new RuntimeException("Super Failure"))

  println(aSuccess)
  println(aFailure)

  def unsafeMethod: String = throw new RuntimeException("No string for you")

  // Try objects via the apply method
  val potentialFailure = Try(unsafeMethod)
  println(potentialFailure)

  // Syntax Sugar
  val anotherPotentialFailure = Try {
    // Code that might throw
  }

  // Utilities
  println(potentialFailure.isSuccess)
  println(potentialFailure.isFailure)

  // orElse
  def backupMethod: String = "A valid result"
  val fallbackTry          = Try(unsafeMethod) orElse Try(backupMethod)
  println(fallbackTry)

  // If you design an API
  def betterUnsafeMethod: Try[String] = Failure(throw new RuntimeException)
  def betterBackupMethod: Try[String] = Success("A valid result")

  def betterFallback: Try[String] = betterUnsafeMethod orElse betterBackupMethod

  // Map, FlatMap, Filter
  println(aSuccess.map(_ * 2))
  println(aSuccess.flatMap(n => Success(n * 10)))
  println(aSuccess.filter(_ > 10)) // Turn into a failure

  /** Exercise
    */
  val hostName = "localhost"
  val port     = "8080"

  def renderHTML(page: String): Unit = println(page)
  class Connection {
    def get(url: String): String = {
      val random = new Random(System.nanoTime)
      if (random.nextBoolean) "<html>...</html>"
      else throw new RuntimeException("Connection interrupted")
    }
  }

  object HttpService {
    val random                                                = new Random(System.nanoTime)
    def getConnection(host: String, Port: String): Connection = {
      if (random.nextBoolean) new Connection
      else throw new RuntimeException("Port toked")
    }
  }

  val connectionStatus = for {
    connection <- Try(HttpService.getConnection(hostName, port))
    html       <- Try(connection.get(""))
  } yield renderHTML(html)
}
