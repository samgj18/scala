package lectures.part3

import scala.util.Random

object Options extends App {
  val myFirstOption: Option[Int] = Some(4)
  val noOption: Option[Int]      = None

  println(myFirstOption)

  // Unsafe API's
  def unsafeMethod: String = null
  //val result = Some(unsafeMethod) // This is wrong never do
  val result               = Option(unsafeMethod)
  println(result)

  //Chained methods
  def backupMethod: String = "A valid result"
  val chainedResult        = Option(unsafeMethod).orElse(Option(backupMethod))

  //Design unsafe API's
  def betterUnsafeMethod: Option[String] =
    None

  def betterBackupMethod: Option[String] =
    Some("A valid result")

  val betterChainedMethod = betterUnsafeMethod orElse betterBackupMethod

  // Functions on Options
  println(myFirstOption.isEmpty)
  println(myFirstOption.get) // Unsafe || NEVER USE

  // Map, FlatMap, Filter
  println(myFirstOption.map(_ * 2))
  println(myFirstOption.filter(_ > 10))
  println(myFirstOption.flatMap(n => Option(n * 10)))

  // For comprehensions
  /** Exercise
    */
  val config: Map[String, String] = Map(
    "host" -> "localhost",
    "port" -> "8080"
  )

  class Connection {
    def connect = "Connected"
  }

  object Connection {
    val random = new Random(System.nanoTime)

    def apply(host: String, port: String): Option[Connection] =
      if (random.nextBoolean) Some(new Connection)
      else None
  }

  // Try to establish a connection, if so - print the connect method
  val host = config.get("host")
  val port = config.get("port")

  val connectionStatus =
    for {
      host       <- config.get("host")
      port       <- config.get("port")
      connection <- Connection(host, port)
    } yield connection.connect

  println(connectionStatus)
}
