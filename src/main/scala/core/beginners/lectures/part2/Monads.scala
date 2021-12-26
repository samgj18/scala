package lectures.part2

object Monads extends App {

  /**
    * Monad:
    * 1. Ability to wrap a value into another type
    * 2. Ability to transform that type into another kind of the same type
    */
  case class SafeValue[+T](private val internalValue: T) { // "Constructor" = pure, unit
    def get: T =
      synchronized {
        internalValue
      }

    def transform[S](transformer: T => SafeValue[S]): SafeValue[S] =
      synchronized { // bind, or flatmap
        transformer(internalValue)
      }
  }

  // "External" API
  def gimmeSomeValue[T](value: T): SafeValue[T] = SafeValue(value)

  val safeString: SafeValue[String] = gimmeSomeValue("Scala is awesome")
  // Extract
  val string = safeString.get
  // Transform
  val upperString = string.toUpperCase()
  // Wrap
  val upperSafeString = SafeValue(upperString)

  /**
    * Pattern: ETW
    */
  // Compressing into one line of code
  val upperSafeString2 = safeString.transform(s => SafeValue(s.toUpperCase()))

  // Examples

  // Census Example
  case class Person(name: String, lastName: String) {
    assert(name != null && lastName != null)
  }

  // Census API
  def getPerson(name: String, lastName: String): Person = {
    if (name.nonEmpty) {
      if (lastName.nonEmpty) {
        Person(name, lastName)
      } else {
        null
      }
    } else {
      null
    }
  }

  def getPersonBetter(name: String, lastName: String): Option[Person] = {
    Option(name).flatMap { fName =>
      Option(lastName).flatMap { lName =>
        Option(Person(fName, lName))
      }
    }
  }

  def getPersonBetterFor(name: String, lastName: String): Option[Person] = {
    for {
      fName <- Option(name)
      lName <- Option(lastName)
    } yield Person(fName, lName)
  }

  // Monads Properties

  // Left Identity => Monad(x).flatMap(f) == f(x)
  def twoConsecutive(x: Int): Seq[Int] = List(x, x + 1)
  twoConsecutive(3) // List(3, 4)
  List(3).flatMap(twoConsecutive) // List(3, 4)

  // Right Identity => Monad(v).flatMap(x => Monad(x)) == Monad(v)
  List(1, 2, 3).flatMap(x => List(x)) // List(1, 2, 3)

  // Associativity ETW => Monad(v).flatMap(f).flatMap(g) == Monad(v).flatMap(x => f(x).flatMap(g))
  val incrementer = (x: Int) => List(x, x + 1)
  val doubler = (x: Int) => List(x, 2 * x)
  val numbers = List(1, 2, 3)

  // Equals
  numbers.flatMap(incrementer).flatMap(doubler)
  numbers.flatMap(x => incrementer(x).flatMap(doubler))
  // List(1, 2, 2, 4,    2, 4, 3, 6     3, 6, 4, 8)
  /*
    List(
      incrementer(1).flatMap(doubler) -- 1, 2, 2, 4
      incrementer(1).flatMap(doubler) -- 2, 4, 3, 6
      incrementer(1).flatMap(doubler) -- 3, 6, 4, 8
    )
   */

  // Option, Future, Either, and more or less all the collection types such as List, Tree, and Map, to name a few, are monads.
}
