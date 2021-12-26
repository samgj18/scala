package lectures.part2

import java.lang

object Exceptions extends App {
  val x: String = null

  // println(x.length) -> Throws with a null pointer exception

  // Throwing and catching exceptions

  // 1. Throwing exceptions

  // Exceptions are instances of classes

  // val aWeirdValue  = throw new NullPointerException

  // Throwable classes extend the Throwable class.
  // Exception(Something wrong with the program) and Error(Something wrong with the system i.e. Stack Overflow) are the major Throwable subtypes.

  // 2. Catching exceptions

  def getInt(withExceptions: Boolean): Int = {
    if (withExceptions) throw new RuntimeException("No int for you!")
    else 42
  }

  try {
    getInt(true)
  } catch {
    case e: RuntimeException =>
      println(s"Caught a Runtime Exception ${e.getMessage}")
  } finally {
    // Code will get executed no matter what!. This block is Optional and does not influence the return type of the expression
    // Use finally only for Side Effects (Logging)
    println("Finally")
  }

  // 3. How to define our own exceptions
  class MyException extends Exception

  val exception = new MyException

  /**
    * 1. Crash your program with an OutOfMemoryError
    * 2. Crash with SOError
    * 3. PocketCalculator
    *  - add(x, y)
    *  - subtract(x, y)
    *  - multiply(x, y)
    *  - divide(x,y)
    *
    *  Throw
    *   - OverflowException if add(x,y) exceeds Int.MAX_VALUE
    *   - UnderflowException if subtract(x,y) exceeds Int.MIN_VALUE
    *   - MathCalculationException for division by 0
    */

  // OOM
  // 1. val array =  Array.ofDim(Int.MaxValue)

  // 2. SO Exception
  def infinite: Int = 1 + infinite

  // val noLimit = infinite

  class OverflowException extends RuntimeException
  class UnderflowException extends RuntimeException

  object PocketCalculator {
    def add(x: Int, y: Int): Int = {
      val result = x + y
      if (x > 0 && y > 0 && result < 0) throw new OverflowException
      else if (x < 0 && y < 0 && result > 0) throw new UnderflowException
      else result
    }

    def subtract(x: Int, y: Int): Int = {
      val result = x - y
      if (x > 0 && y < 0 && result < 0) throw new OverflowException
      else if (x < 0 && y > 0 && result > 0) throw new UnderflowException
      else result
    }

    def multiply(x: Int, y: Int): Int = {
      val result = x * y
      if (x > 0 && y > 0 && result < 0) throw new OverflowException
      else if (x < 0 && y < 0 && result < 0) throw new OverflowException
      else if (x > 0 && y < 0 && result > 0) throw new UnderflowException
      else if (x < 0 && y > 0 && result > 0) throw new UnderflowException
      else result
    }
  }
}
