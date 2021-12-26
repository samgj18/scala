package lectures.part1

object Functions extends App {
  def aFunction(a: String, b: Int): String =
    a + " " + b
    
  def aParameterlessFunction(): Int = 42
  
  def aRepeatedFunction(aString: String, n: Int): String = {
    if (n == 1) aString
    else aString + aRepeatedFunction(aString, n - 1)
  }
  
  // WHEN YOU NEED LOOPS, USE RECURSION!
  
  def aFunctionWithSideEffects(aString: String): Unit = println(aString)
  
  def aBigFunction(n: Int): Int = {
    def aSmallerFunction(a: Int, b: Int): Int = a + b
    
    aSmallerFunction(n, n - 1)
  }
  
  /*
    Exercises:
      1. A greeting function(name, age) => "Hi my name is $name, I'm $age years old"
      2. Factorial function 1 * 2 * 3 ... * n
      3. A Fibonacci function
        f(1) = 1
        f(2) = 1
        f(n) = f(n - 1) f(n - 2)
      4. Tests if number is prime
   */
  
  def aGreetingFunction(name: String, age: String): String = s"Hi my name is $name, and I'm $age years old"
  
  def aFactorialFunction(n: Int): Int = if (n == 0) 1 else n * aFactorialFunction(n - 1)
  
  def aFibonacciFunction(n: Int): Int = if (n <= 2) 1 else aFibonacciFunction(n - 1) + aFibonacciFunction(n - 2)

  def aTestPrimeNumber(n: Int): Boolean = {
    def isPrimeUntil(t: Int): Boolean =
      if (t <= 1) true
      else n % t != 0 && isPrimeUntil(t - 1)

    isPrimeUntil(n / 2)
  }
}
