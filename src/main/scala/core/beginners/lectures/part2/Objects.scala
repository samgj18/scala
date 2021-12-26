package lectures.part2

object Objects extends App {
  //Scala does not have Class Level Functionality ("static")

  /**
    * Objects are their own type (Person in this case)
    * and their only instance
    */

  /**
    * Having an object and a class with the same name in the same file
    * or scope is called Companions.
    *
    * Benefits: The whole code will reside in either a class or a Singleton
    * Object.
    */
  object Person { //Objects do not receive parameters
    // "Static"/ "Class" - Level Functionality

    val N_EYES = 2 //Class level functionality
    def canFly: Boolean = false

    def apply(mother: Person, father: Person): Person =
      new Person("Bobby") // Factory method
  }
  class Person(val name: String) {
    //  Instance-Level functionality
  }

  println(Person.N_EYES)
  println(Person.canFly)

  // Scala Object is a Singleton Instance
  val mary = Person
  val john = Person

  println(mary == john) // true

  val karl = new Person("karl")
  val lola = new Person("lola")

  // println(karl == mary) // false

  val bobbie = Person(lola, karl)

  // Scala applications = Scala Object with def main(args: Array[String]): Unit implemented

}
