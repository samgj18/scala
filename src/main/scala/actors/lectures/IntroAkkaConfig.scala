package actors.lectures

import akka.actor.Actor
import akka.actor.ActorLogging
import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import akka.actor.Props

object IntroAkkaConfig extends App {

  /** Different ways to pass a configuration
    *
    *   1. Inlined Configuration
    */

  class SimpleLoggingActor extends Actor with ActorLogging {

    override def receive: Receive = { case message =>
      log.info(message.toString)
    }

  }

  val configuration =
    """
   |akka {
   |  loglevel = ERROR
   |}
   """.stripMargin

  val config = ConfigFactory.parseString(configuration)
  // val system = ActorSystem("ConfigurationDemo", config)
  // val actor1 = system.actorOf(Props[SimpleLoggingActor])
  //

  /** 2. Configuration file from default folder located at
    *
    * root ~ resources ~ application.conf
    *
    * BEWARE THAT THE NAMING IS IMPORTANT
    */

  // val system = ActorSystem("ConfigurationFromApplicationConf")

  /** 3. Separate config from a different namespace
    */

  val configuration2 =
    """
   |akka {
   |  loglevel = ERROR
   |}
   |namespace {
   |  loglevel = INFO
   |}
   """.stripMargin

  val specialConfig = ConfigFactory.load().getConfig("namespace")
  val specialSystem = ActorSystem("Special", specialConfig)
  val actor2        = specialSystem.actorOf(Props[SimpleLoggingActor])

  /** 4. Separate configuration on a different file
    */
  // val specialConfig = ConfigFactory.load("newPath/file.conf")

  /** 5. .json files and .properties files can be used as well
    */
  // val specialConfig = ConfigFactory.load("file.json") // ConfigFactory.load("file.properties")
}
