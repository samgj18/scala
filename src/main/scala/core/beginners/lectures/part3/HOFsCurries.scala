package lectures.part3

import scala.annotation.tailrec

object HOFsCurries extends App {
  // val superFunction: (Int, (String, (Int => Int)) => Int) => (Int => Int) = ???
  // Higher Order Function (HOF)
  // map, flatMap, filter (HOFs)

  // Function that applies n times another function over a given value
  /**
    * nTimes(f, n, x)
    */
  @tailrec
  def nTimes(f: Int => Int, n: Int, x: Int): Int =
    if (n <= 0) x
    else nTimes(f, n - 1, f(x))

  val plusOne: (Int => Int) = _ + 1
  println(nTimes(plusOne, 4, 1))

  def nTimesBetter(f: Int => Int, n: Int): Int => Int =
    if (n <= 0) (x: Int) => x
    else (x: Int) => nTimesBetter(f, n - 1)(f(x))

  val plus10 = nTimesBetter(plusOne, 4)
  println(plus10(1))

  // Curried Functions
  val superSum: Int => (Int => Int) =
    (x: Int) =>
      (y: Int) => x + y // val superSum: Int => (Int => Int) = x => y => x + y

  val sum3 = superSum(3) // x => 3 + x
  println(sum3(3))

  // Function with multiple parameter lists

  def curriedFormat(c: String)(x: Double): String =
    c.format(x)

  val standardFormat: (Double => String) = curriedFormat("%4.2f")
  val preciseFormat: (Double => String) = curriedFormat("%10.8f")

  println(standardFormat(Math.PI))
  println(preciseFormat(Math.PI))

  /**
    * 1. Expand GenericMyList
          - foreach function A => Unit
            [1, 2, 3].foreach(x => println(x))

          - sort function ((A, A) => Int) => GenericMyList
            [1, 2, 3].sort((x, y) => y - x) => [3, 2, 1]

          - zipWith (list, (A, A) =>B) => GenericMyList[B]
            [1, 2, 3].zipWith([4, 5, 6], x * y) => [1 * 4, 2 * 5, 3 * 6] = [4, 10, 18]

          - fold(start)(function) => value
            [1, 2, 3].fold(0)(x + y) = 6

    * 2. toCurry(f: (Int, Int) => Int): (Int => Int => Int)
         fromCurry(f: (Int => Int => Int)) => (Int, Int) => Int


    * 3. compose(f, g) => x => f(g(x))
         andThen(f, g) => x => g(f(x))
    */

  def toCurry(f: (Int, Int) => Int): (Int => Int => Int) = x => y => f(x, y)

  def fromCurry(f: (Int => Int => Int)): (Int, Int) => Int = (x, y) => f(x)(y)

  def compose[A, B, C](f: A => B, g: C => A): C => B = x => f(g(x))

  def andThen[A, B, C](f: A => B, g: B => C): A => C = x => g(f(x))

  def superAdder: Int => Int => Int = toCurry(_ + _)

  def add4 = superAdder(4)

  println(add4(18))
}
