package lectures.part2

import java.time.LocalDate

object OOBasics extends App {
  val person = new Person("Samuel", 24)
  println(person.age)
  person.greet("Jhon")

  val counter = new Counter
  counter.inc.inc.inc.print
  counter.inc(10).print
}

// Constructor
class Person(name: String, val age: Int = 0) {
  // Body
  //Val, Var, Def
  val aVal = 2
  println(aVal)

  def greet(name: String): Unit = println(s"${this.name} says hello to $name")

  //Overloading: Methods same name but different signatures
  def greet(): Unit = println(s"Hi my name is $name")

  //Overloading Constructor -  Can only be called with another constructor
  def this(name: String) = this(name, 0)
}

/*
 Class parameters from the constructor are not fields,
 the way to do it is add a val before the parameter
 */

/** Implement a Novel and a Writer class
  *
  * Writer: first name, surname, year of birth
  *   - fullname -> Concat of name and surname
  *
  * Novel: name, year of release, author: Writer
  *   - authorAge -> Age of the author at the year of release
  *   - isWrittenBy(author)
  *   - copy(new year of release) -> New instance of Novel with the new year of release
  */

class Writer(name: String, surname: String, val year: Int) {
  def fullname = s"$name $surname" //name.concat(" ".concat(surname))
}

class Novel(name: String, year: Int, author: Writer) {
  def authorAge: Int = year - author.year

  def isWrittenBy(author: Writer): Boolean = author == this.author

  def copy(year: Int): Novel = new Novel(name, year = year, author)
}

/** Implement a Counter Class
  *   - receives an int value
  *   - method current count
  *   - method to increment and decrement => new Counter
  *   - overload inc/dec to receive amount
  */

class Counter(val count: Int = 0) {
  // def currentCount = n This is called a getter and it could be improved in Scala by turning count into a val
  def inc: Counter = {
    println("increment")
    new Counter(count + 1)
  }

  def dec: Counter = {
    println("decrement")
    new Counter(
      count - 1
    ) //Immutability: whenever we need to modify a value of an instance we need to return a new instance of the class
  }

  // Overload

  def inc(n: Int): Counter = {
    if (n <= 0) this
    else inc.inc(n - 1)
  }

  def dec(n: Int): Counter = {
    if (n <= 0) this
    else dec.dec(n - 1)
  }

  def print(): Unit = println(count)
}
