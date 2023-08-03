package lectures.part2

object AbstractDataTypes extends App {

  // Situations where we need to leave some methods fields or methods, cannot be instantiated
  abstract class Animal {
    val creatureType: String
    def eat: Unit
  }

  class Dog extends Animal {
    val creatureType: String = "Canine"
    def eat: Unit            = println("crunch crunch")
  }

  /** Traits - Ultimate abstract DataType Can be inherited along classes
    */
  trait Carnivore {
    def eat(animal: Animal): Unit
  }

  class Crocodile extends Animal with Carnivore {
    override val creatureType: String      = "crock"
    override def eat: Unit                 = println("nomnomnom")
    override def eat(animal: Animal): Unit = println(s"I'm a crock and I'm eating a(n) ${animal.creatureType}")
  }

  val dog  = new Dog
  val croc = new Crocodile

  croc.eat(dog)

  /** Differences between traits and abstract classes.
    *
    * Abstract and Traits can have abstract and non abstract values.
    *   1. Traits -> Do not have constructor parameters 2. You can only extend one class but mixin multiple Traits 3.
    *      Traits are behavior, Abstract are things.
    */
}
