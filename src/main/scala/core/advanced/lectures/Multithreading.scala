package core.advanced.lectures

import scala.util.Success
import scala.util.Failure
import scala.concurrent.Promise

object Multithreading extends App {
  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("Running in parallel")
  })

  aThread.start()
  aThread.join()

  val threadHello   = new Thread(() => (1 to 1000).foreach(_ => println("Hello")))
  val threadGoodbye = new Thread(() => (1 to 1000).foreach(_ => println("Goodbye")))

  threadHello.start()
  threadGoodbye.start()

  // different runs produce different results!

  // Volatile does something similar to synchronized
  // but only works for primitive types. For a deeper explanation
  // check concurrent programming in scala book
  class BankAccount(@volatile private var amount: Int) {
    override def toString: String = "" + amount

    def withdraw(money: Int): Unit = this.amount -= money

    def safeWithdraw(money: Int): Unit = this.synchronized {
      this.amount -= money
    }
  }

  // inter-thread communication on the JVM
  // wait - notify mechanism

  // Scala Futures
  // This is a ForkJoinPool under the hood
  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.Future

  val aFuture = Future {
    // long computation here
    42
  }

  aFuture.onComplete {
    case Success(value)     => println(s"Got the callback, meaning = $value")
    case Failure(exception) =>
      println(s"Got the callback, exception = $exception")
  }

  // Promises complete Futures manually
  // Promises are a "controller" over a Future
  val aPromise = Promise[Int]()

  val futureFromPromise = aPromise.success(42).future

  futureFromPromise.onComplete {
    case Success(value)     => println(s"Got the callback, meaning = $value")
    case Failure(exception) =>
      println(s"Got the callback, exception = $exception")
  }
}
