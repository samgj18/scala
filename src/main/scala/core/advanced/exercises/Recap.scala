import scala.annotation.tailrec
object Recap extends App {
  val aCondition        = true
  val aConditionedValue = if (aCondition) 5 else 3

  // Instructions (vs Expressions)
  // Instructions are executed, expressions are evaluated

  val aCodeBlock = {
    val y = 2
    val z = y + 1

    if (z > 2) "hello" else "goodbye"
  }

  // Unit, equivalent to void in Java
  val theUnit = println("Hello, Scala")

  // Functions, first class citizens in Scala (can be passed around)
  // and can be used as values (can be assigned to variables) and can be passed as parameters to other functions (can be returned from functions)
  def aFunction(x: Int): Int = x + 1

  // Recursion: stack and tail-recursion (tail-recursion is preferred). Tail-recursion is more efficient, they are converted to loops in the JVM (Java Virtual Machine) bytecode
  def factorial(n: Int): Int = {
    if (n <= 0) 1
    else n * factorial(n - 1)
  }

  @tailrec
  def factorialTailRec(n: Int, acc: Int): Int = {
    if (n <= 0) acc
    else factorialTailRec(n - 1, n * acc)
  }

  // Object Orientation Programming
  class Animal
  class Dog extends Animal
  val aDog: Animal = new Dog // subtyping polymorphism

  trait Carnivore {
    def eat(a: Animal): Unit
  }

  class Crocodile extends Animal with Carnivore {
    override def eat(a: Animal): Unit = println("crunch!")
  }

  // Method Notations

  val aCroc = new Crocodile
  aCroc.eat(aDog)
  aCroc eat aDog

  // Anonymous Classes, the compiler will generate an anonymous class for you by extending Carnivore and overriding the eat method and assigning a new instance of the anonymous class to aCarnivore
  val aCarnivore = new Carnivore {
    override def eat(a: Animal): Unit = println("roar!")
  }

  // Generics
  abstract class MyList[+A] // variance and variance problems in Scala
  // Singletons and companions (companions are objects that are created along with the class) (companions are used to access the private members of the class)
  object MyList

  // Case Classes - immutable and serializable (can be used as keys in maps) and have a nice toString method defined in the companion object (can be used in pattern matching) and have a copy method defined in the companion object
  case class Person(name: String, age: Int)

  // Exceptions and Try/Catch/Finally - Scala's exception handling mechanism is based on the concept of stack traces. A stack trace is a list of methods that were called to get to the place where the exception was thrown.
  // The stack trace is used to find the place where the exception was thrown and to find the code that could have potentially caused the exception.
  lazy val throwsException =
    throw new RuntimeException // throwsException: Nothing, cannot be instantiated
  lazy val aPotentialFailure =
    try {
      throw new RuntimeException
    } catch {
      case e: Exception => "I caught an exception"
    } finally {
      println("some logs")
    }

  // Functional Programming
  val incrementer = new Function1[Int, Int] {
    override def apply(v1: Int): Int = v1 + 1
  }

  incrementer(1) // 2

  val anonymousIncrementer = (x: Int) => x + 1 // equivalent to the above
  List(1, 2, 3).map(
    anonymousIncrementer
  ) // List(2, 3, 4) - map applies the function to each element of the list, HOF (higher-order function)

  // map, flatMap, filter
  // map applies the function to each element of the list, flatMap applies the function to each element of the list and flattens the result into a single list (flatten is a method on lists) and filter filters the list based on the function
  // map and flatMap are the two core methods of functional programming in Scala
  // filter is a special case of map and flatMap
  // map and flatMap are also known as functors

  // for-comprehensions
  val pairs = for {
    num <- List(1, 2, 3)   // num is the name of the iterator variable
    char <- List('a', 'b', 'c')
  } yield num + "-" + char // yield is the name of the result variable

  // Scala Collections: Seqs, Arrays, Lists, Vectors, Maps, Tuples
  val aMap = Map(
    "Daniel" -> 789,
    "Jess"   -> 555
  )

  // "collections": Options, Try
  val anOption = Some(2)

  // Pattern Matching
  val x     = 2
  val order = x match {
    case 1 => "first"
    case 2 => "second"
    case 3 => "third"
    case _ => x + "th"
  }

  val bob = Person("Bob", 22)

  val greeting = bob match {
    case Person(n, _) => s"Hi, my name is $n"
  }
}
