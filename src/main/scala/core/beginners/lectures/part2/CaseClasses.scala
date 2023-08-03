package lectures.part2

object CaseClasses extends App {
  case class Person(name: String, age: Int)

  // 1. Class parameters are promoted to fields

  val jim = new Person("Jim", 34)
  println(jim.name)

  // 2. Sensible toString
  // println(instance) = println(instance.toString)
  println(jim.toString)

  // 3. equals and hashCode implemented OOTB
  val jim2 = new Person("Jim", 34)
  println(jim == jim2)

  // 4. CCs have handy copy method
  val jim3 = jim.copy(age = 45)
  println(jim3)

  // 5. CCs have companion objects

  val thePerson = Person
  val mary      = Person("Mary", 43)

  // 6. CCs are serializable

  // 7. CCs have extractor patterns = CCs can be used in PATTERN MATCHING

  // Same properties as CCs but the companion objects because they are companion objects
  case object UnitedKingdom {
    def name: String = "The UK of GB and NI"
  }
}
