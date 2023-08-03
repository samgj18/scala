package lectures.part3

import scala.util.Random

object Sequences extends App {
  // Seq
  val aSequence = Seq(1, 2, 3, 4, 6, 5)
  println(aSequence) // Print a List because has an apply factory method
  println(aSequence.reverse)
  println(aSequence(2))
  println(aSequence ++ Seq(5, 6, 7))
  println(aSequence.sorted)

  // Ranges
  val aRange: Seq[Int] = 1 to 10 // 1 until 10 is non inclusive
  aRange.foreach(println)

  // Links
  val aList             = List(1, 2, 3)
  val prepended         = 42 :: aList
  val prependedAppended = 45 +: aList :+ 43
  println(prependedAppended)

  val apples5 = List.fill(5)("apple") // Curried function
  println(apples5)
  println(aList.mkString("-")) // Concatenates all the values

  // Arrays
  val anArray       = Array(1, 2, 3, 4)
  val threeElements = Array.ofDim[String](3)
  println(threeElements.mkString("Array(", ", ", ")"))

  /** Array mutation
    */
  anArray(2) = 0 // Syntax sugar for anArray.update(2, 0)
  println(anArray.mkString(" "))

  // Array and Seq
  val numbersSeq: Seq[Int] = anArray // Implicit conversion
  println(numbersSeq)

  // Vector
  val aVector: Vector[Int] = Vector(1, 2, 3)

  val maxCapacity = 1000000
  val maxRuns     = 1000

  // Vector vs List
  def getWriteTime(collection: Seq[Int]): Double = {
    val r     = new Random
    val times = for {
      it <- 1 to maxRuns
    } yield {
      val currentTime = System.nanoTime()
      collection.updated(r.nextInt(maxCapacity), r.nextInt)
      System.nanoTime() - currentTime
    }

    times.sum * 1.0 / maxRuns
  }

  val numbersList   = (1 to maxCapacity).toList
  val numbersVector = (1 to maxCapacity).toVector

  // Keeps reference to tails
  // Updating an element in the middle takes a long time
  println(getWriteTime(numbersList))

  // Depth of the tree is small
  // It needs to replace an entire 32 element chunk
  println(getWriteTime(numbersVector))

  case class Person(id: String, name: String, lastName: String)

  val aThing = Seq(
    Person("1", "Sameul", "Goa"),
    Person("2", "Sameul", "Gomez"),
    Person("3", "Sameul", "Gomez"),
    Person("4", "Sameul", "Gomez")
  )

  aThing.collect { case person: Person =>
    if (person.id == "1") Some(person) else None
  }.flatten
}
