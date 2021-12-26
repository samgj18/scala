package lectures.part2

object AnonymousClasses extends App {
  abstract class Animal {
    def eat(): Unit
  }

  // AnonymousClass
  val funnyAnimal: Animal = new Animal {
    override def eat(): Unit = println("ahahahahaha")
  }

  // What does the compiler do?
  class AnonymousClasses$$anon$1 extends Animal {
    override def eat(): Unit = println("ahahahahaha")
  }

  val funnnyAnimalCompiler: Animal = new AnonymousClasses$$anon$1

  println(funnyAnimal.getClass)

  class Person(name: String) {
    def sayHi(): Unit = println(s"Hi, my name is $name")
  }

  // Anonymous classes work for abstract classes and non abstract classes as well
  val jim = new Person("Jim") {
    override def sayHi(): Unit = println("Hi my name is Jim, how can I help you?")
  }

}
