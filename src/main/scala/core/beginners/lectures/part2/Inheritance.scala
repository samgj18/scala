package core.beginners.lectures.part2

object Inheritance extends App {

  // Single class Inheritance
  class Animal {
    val creatureType = "wild"
    val monster      = false

    def breaths         = "never"
    def eat             = println("Ñom Ñom")
    private def fly     = false // Not accessible outside
    protected def walks = true  // Accessible only usable within this class and subclasses

    // How to prevent overrides
    final def isAlive = false // Can't be override
    // 2. Using final on the class itself, in that case any method can be override
    // 3. Seal the class -> Extend classes in THIS FILE, prevent extension in other files (kw sealed)

  }
  class Cat extends Animal { // Inherits all the non private fields and methods
    def crunch = {
      walks
      println("cronch cronch")
    }
  }

  val cat = new Cat
  cat.eat

  // Constructors

  class Person(name: String, age: Int) {
    def this(name: String) = this(name, 0)
  }

  /** Extending a class with parameters Age is option because I override the constructor with age = 0
    * @param name
    * @param age
    * @param idCard
    */
  class Adult(name: String, age: Int, idCard: String)
      extends Person(name, age) // Compiler calls the constructor of Person before Adult

  // Overriding
  class Dog(override val monster: Boolean) extends Animal {
    override val creatureType: String = "domestic"
    override def walks                = true

    override def breaths: String = {
      super.breaths // Will refer to the method in the super class
      "people"
    }
  }
  val dog = new Dog(true)
  println(dog.walks)
  println(dog.creatureType)

  // Type Substitution (Broad: Polymorphism) -> A method call wil always go to the most recent override version
  val unknownAnimal: Animal = new Dog(false)
  unknownAnimal.eat // Returns crunch crunch

  // Let's talk about super
}
