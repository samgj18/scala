package lectures.part3

object WhatsAFunction extends App {
  // Use functions as first class elements
  val doubler = new MyFunction[Int, Int] {
    override def apply(element: Int): Int = element * 2
  }
  // Function types = Function1[A, B]

  val stringToIntConverter = new Function[String, Int] {
    override def apply(string: String): Int = string.toInt
  }
  println(stringToIntConverter("3") + 4)

  // Function2[Int, Int, Int] === (Int, Int) => Int
  val adder: Function2[Int, Int, Int] = new Function2[Int, Int, Int] {
    override def apply(a: Int, b: Int): Int = a + b
  }

  // Function[A, B, R] => (A, B) => R

  /*
    All Scala functions are objects
   */
  /**   1. Write a function that takes two strings a concatenates them 2. Transform myPredicate and myTransformer into
    *      function types 3. Define a function which takes an int and returns another function which takes an int and
    *      returns another int
    *   - What's the type of the function?
    *   - How to do it?
    */
  def concatenate: (String, String) => String =
    new Function2[String, String, String] {
      override def apply(from: String, to: String): String = from + to
    }

  def superSum: Int => Function1[Int, Int] = {
    new Function[Int, Function1[Int, Int]] {
      override def apply(value: Int): Int => Int =
        new Function[Int, Int] {
          override def apply(inner: Int): Int = inner + value
        }
    }
  }

  val adder3: Int => Int = superSum(3)
  println(adder3(4)) // Result is 7
  println(
    superSum(3)(8)
  )                  // Result is 11 => CURRIED FUNCTION: Function that takes parameters and return another function that receives
}

trait MyFunction[A, B] {
  def apply(element: A): B
}
