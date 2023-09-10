package cats.lectures

import scala.annotation.nowarn

object CatsIntro extends App {
  // Eq
  @nowarn
  val aComparison = 2 == "a string"

  // Part 1 - Typeclass import
  import cats.Eq

  // Part 2 - Import TC instances for the types you need (TC = TypeClass)
  import cats.instances.int._

  // Part 3 - Use the TC API
  val intEquality         = Eq[Int]
  val aTypeSafeComparison = intEquality.eqv(2, 3) // false
  // val anUnsafeComparison = intEquality.eqv(2, "a string") // does not compile

  // Part 4 - Use extension methods (if applicable)
  import cats.syntax.eq._

  val anotherTypeSafeComparison = 2 === 3 // false
  val neqComparison             = 2 =!= 3 // true
  // val invalidComparison = 2 === "a string" // does not compile

  // Part 5 - Extend the TC with implicit
  val aListComparison = List(2) === List(3) // false

  // There's no need to import cats.instances.list._ because it is now embedded in
  // cats.syntax.eq._

  case class ToyCar(model: String, price: BigDecimal)

  implicit val toyCarEq: Eq[ToyCar] = Eq.instance[ToyCar] { (car1, car2) =>
    car1.model == car2.model && car1.price == car2.price
  }

  val toyCar1 = ToyCar("Ferrari", 100000)
  val toyCar2 = ToyCar("Ferrari", 100000)

  println(toyCar1 === toyCar2) // true
}
