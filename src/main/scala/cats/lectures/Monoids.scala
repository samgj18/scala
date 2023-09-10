package cats.lectures

trait Semigroups[A] {
  // A semigroup is a typeclass for a type A that defines a binary operation which results on another instance of the same type.

  /** A binary operation which results on another instance of the same type.
    *
    * @param x
    * @param y
    * @return
    */
  def combine(x: A, y: A): A

  // There are certain laws associated with typeclasses. In the case of semigroups, the laws are:
  // Associativity: (x combine y) combine z = x combine (y combine z)

}

trait Monoids[A] {
  // A monoid is just a Semigroup[A] plus a neutral element that is the identity of the binary operation.
  def empty: A
  def combine(x: A, y: A): A

  // There are certain laws associated with typeclasses. In the case of monoids, the laws are:
  // Associativity: (x combine y) combine z = x combine (y combine z)
  // Left Identity: x combine empty = x
  // Right Identity: empty combine x = x
}

object AMonoid extends App {
  import cats.implicits._
  import cats.kernel.Monoid

  println("Normal Sum", 1 + 2 + 3 + 0)
  println(
    "Cats Int Monoid",
    1 |+| 2 |+| 3 |+| Monoid[Int].empty
  ) // |+| is the combine method

  // There other types of Monoids that are available in cats.kernel.Monoid, such as the following:
  println(
    "Cats String Monoid",
    Monoid[String].combine("Hello", "World")
  )

  println(
    "Cats List Monoid",
    Monoid[List[Int]].combine(List(1, 2, 3), List(4, 5, 6))
  )

  println(
    "Cats Option Monoid",
    Monoid[Option[Int]].combine(Option(1), Option(2))
  )

  // Monoids can also be constructed using the Monoid trait.
  implicit val productMonoid: Monoid[Int] = new Monoid[Int] {
    def empty: Int                   = 1
    def combine(x: Int, y: Int): Int = x * y
  }

  println(
    "Cats Product Monoid",
    Monoid[Int].combine(1, 2)
  )

  // Combine all the values in a list using a monoid.

  def toCombine[T: Monoid](list: List[T]) =
    /* list.foldLeft(Monoid[T].empty)(Monoid[T].combine) === list.foldLeft(Monoid[T].empty)(_ |+| _) === list.combineAll*/
    list.combineAll

  // The following example shows how to use the Foldable typeclass to combine all the values in a list.
  println(
    "Plain Scala List Sum",
    List("1", "2", "3", "4", "5").map(_.length).sum
  )

  println(
    "Using the list monoid for ints",
    List("1", "2", "3", "4", "5").map(_.length).combineAll
  )

  // We can do the same thing in a single step using the Foldable typeclass.
  println(
    "Using foldable typeclass",
    List("1", "2", "3", "4", "5").foldMap(
      _.length
    ) // The length function is applied to
    // each element and then combined in an internal accumulator
  )
}
