package lectures.part1

import scala.annotation.tailrec

object Recursion extends App {
  def factorial(n: Int): Int =
    if (n <= 1) 1
    else n * factorial(n - 1)
    
  def anotherFactorial(n: Int): Int = {
    @tailrec
    def factorialHelper(x: Int, acc: Int): Int = if(x < 1) acc else factorialHelper(x - 1, x * acc)
    
    factorialHelper(n, 1) // Tail recursion
  }
  
  // WHEN YOU NEED LOOPS, USE _TAIL_ RECURSION.
  
  /*
  1. Concatenate a String n times
  2. Is Prime Function
  3. Fibonacci Function
   */
  def concatString(n: Int, aString: String): String = {
    @tailrec
    def concatStringHelper(x: Int, acc: String): String = 
      if (x <= 1) acc
      else concatStringHelper(x - 1, aString + acc)
      
    concatStringHelper(n, "Hello")
  }
  
  def isPrimeNumber(n: Int): Boolean = {
    @tailrec
    def isPrimeNumberHelper(t: Int, isStillPrime: Boolean): Boolean = {
      if (!isStillPrime) false
      else if (t <= 1) true
      else isPrimeNumberHelper(t - 1, n % t != 0 && isStillPrime)
    }
    isPrimeNumberHelper(n / 2, true)
  }
  
  def fibonacci(n: Int): Int = {
    @tailrec
    def fibonacciHelper(x: Int, last: Int, nextToLast: Int): Int = {
      if (x >= n) last
      else fibonacciHelper(x + 1, last + nextToLast, last)
    }
    if (n <= 2) 1
    else fibonacciHelper(2, 1, 1)
    
  }
}
