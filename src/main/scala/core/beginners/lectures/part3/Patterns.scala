package lectures.part3

object Patterns extends App {
  // Constants
  val x: Any = "Hello Scala"
  val constant = x match {
    case 1        => "A number"
    case "Scala"  => "A String"
    case true     => "A Boolean"
    case Patterns => "A Singleton object"
  }

  // Match Anything
  val matchAnything = x match {
    case _ => "Whatever"
  }
  val matchVariable = x match {
    case something => s"I've found $something"
  }

  // Tuples
  val aTuple = (1, 2)
  val matchTuple = aTuple match {
    case (1, 1)         => "(1, 1) tuple"
    case (something, 2) => s"Extract $something in the pattern matches"
  }

  val nestedTuple = (1, (2, 3))
  val matchNestedTuple = nestedTuple match {
    case (_, (2, v)) => s"Pattern matching powerful $v, because can be nested"
  }

  // Case Classes - Constructor Pattern
  // List Patterns
  val aList: List[Int] = List(1, 2, 3, 4)
  val matchList = aList match {
    case Nil => "List is empty"
    case ::(head, tl) =>
      s"None empty list with head: $head, and tail length: ${tl.length}"
  }

  val matchStandardList = aList match {
    case List(1, _, _)       => 1 // Extractor - Advanced
    case List(1, _*)         => // VarArg -> List of arbitrary length - Advanced
    case 1 :: List(_)        => 1 // Infix Pattern
    case List(1, 2, 3) :+ 42 => 42 // Infix Pattern
  }

  // Type Specifics
  val unknown: Any = 2
  val matchUnknown = unknown match {
    case list: List[Int] => list
    case int: Int        => int
    case string: String  => string
  }

  // Name binding
  val matchNameBinding = aList match {
    case nonEmptyList @ List(_) =>
      nonEmptyList //Name binding: Name entire patterns

    case List(1, rest @ _*) => rest // More name binding: Name entire patterns
  }

  // Multi-pattern
  val multiPattern = aList match {
    case Nil | ::(0, _) => List(1, 2, 4)
  }

  // If Guards
  val matchIfGuards = aList match {
    case List(_, element, _) if element % 2 == 0 => element
  }

  /** Question
    */

  val numbers = List(1, 2, 3)
  val matchNumbers = numbers match {
    case list: List[Int] => "A list of ints"
    //case listOfString: List[String] => "A list of strings" => JVM deletes because of erasure
    case _ => ""
  }

  // Big Idea #1
  /** Catches are actually matches!
    */
  try {
    //Code
  } catch {
    case e: RuntimeException       => "Runtime"
    case npe: NullPointerException => "NPE"
    case _: Throwable              => "Something else"
  }

  // Big Idea #2
  /** Generators are based on pattern matching
    */
  val aNewList = List(1, 2, 3, 4)
  val evenOnes = for {
    even <- aNewList if even % 2 == 0
  } yield even * 10

  val tuple = List((1, 2), (3, 4))

  val filteredTuple = for {
    (first, second) <- tuple
  } yield first * second

  // Big Idea #3
  /** Based on pattern matching
    */
  val tuples = (1, 2, 3)
  val (a, b, c) = tuples
}
