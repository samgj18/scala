package core.beginners.lectures.part2

object Generics extends App {

  // Generic class
  class MyList[+A] {
    def add[B >: A](element: B): MyList[B] = ???
    /*
    A = CAT
    B = DOG -> ANIMAL

     */
  }

  // Generics
  class MyMap[Key, Value]

  val listOfIntegers = new MyList[Int]
  val listOfStrings = new MyList[String]

  object MyList {
    def empty[A]: MyList[A] = ???
  }

  def emptyListOfIntegers: MyList[Int] = MyList.empty[Int]

  // Variance Problem
  class Animal
  class Cat extends Animal
  class Dog extends Animal

  // Question => If Cat extends Animal..

  // 1. List[Cats] extends List[Animal] = COVARIANCE

  class CovariantList[+A]

  val animal: Animal = new Cat
  val animalList: CovariantList[Animal] = new CovariantList[Cat]
  // animalList.add(new Dog) Would this work? -> Yes, we return a list of Animal

  // 2. List[Cats] don't extend List[Animal] = INVARIANCE
  class InvariantList[A]
  val invariantAnimalList: InvariantList[Animal] = new InvariantList[Animal]

  // 3. CONTRAVARIANCE
  class ContravariantList[-A]
  val contravariantList: ContravariantList[Cat] = new ContravariantList[Animal]

  // Example
  class Trainer[-A]
  val trainer: Trainer[Cat] = new Trainer[Animal]

  // Bounded types
  class Cage[A <: Animal](animal: Animal) // Upper bounded -> Class Cage only accepts subtypes of Animal
  val cage = new Cage(new Dog)

  class Jail[A >: Animal](animal: Animal) // Upper bounded -> Class Cage only accepts supertypes of Animal

}
