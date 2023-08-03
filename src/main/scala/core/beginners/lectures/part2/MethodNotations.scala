package lectures.part2
import scala.language.postfixOps

object MethodNotations extends App {

  class Person(val name: String, favoriteMovie: String, val age: Int = 0) {
    def likes(movie: String): Boolean = movie == favoriteMovie

    def hangoutWith(person: Person): String = s"${this.name} is hanging out with ${person.name}"

    def unary_! : String = s"$name what the heck!"

    def unary_+ : Person = new Person(this.name, this.favoriteMovie, age + 1)

    def isAlive: Boolean = true

    def apply(): String = s"Hi my name is $name"

    def hangoutWith(nickname: String): Person = new Person(s"${this.name} the ($nickname)", this.favoriteMovie)

    def learns(subject: String): String = s"${this.name} learns $subject"

    def learnScala = learns("Scala")

    def apply(number: Int): String = s"${this.name} watch her favorite movie inception $number times."
  }

  val mary = new Person("Mary", "Inception")
  println(mary.likes("Inception"))
  println(mary likes "Inception") // Equivalent. Infix notation style and only works with one parameter methods.

  // Operators

  val tom = new Person("Tom", "Fight Clubs")

  println(mary hangoutWith tom)

  // All operators are methods

  // Prefix notation: All about unary operator only works with + - ~ !

  val x = -1
  val y = 1.unary_-

  println(!mary)
  println(mary.unary_!)

  // The unary_prefix only work with few operators

  // Postfix notation: Works with function that receives no parameters

  println(mary.isAlive)
  //println(mary isAlive)

  // Apply
  println(mary.apply)
  println(mary())

  /**   1. Overload the hangoutWith operator mary hangoutWith "the rockstar" => new Person("Mary (the rockstar)")
    *
    * 2. Add an age to the Person class with default of 0. Add unary + operator => returns new person with age + 1
    *
    * 3. Add a learns method in the Person class => Mary learns $string
    *
    * 4. Add a learnsScala method, calls learns method with "Scala". Use it in post notation.
    *
    * 5. Overload apply method => mary.apply(2) => "Mary watch her favorite movie inception 2 times."
    */

  // 1. Overload the hangoutWith operator
  println((mary hangoutWith "rockstar")())
  println((+mary).age) // Returns 1
  println(+mary.age)   // Returns 1
  println(mary learnScala)

}
