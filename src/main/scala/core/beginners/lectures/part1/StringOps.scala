package core.beginners.lectures.part1

object StringOps extends App {
  val str: String = "Hello, learning Scala"
  println(str.charAt(2))
  println(str.substring(7, 11))
  println(str.split(" ").toList)
  println(str.startsWith("Hello"))
  println(str.replace(" ", " - "))
  println(str.toUpperCase)
  println(str.length)

  val aNumberString = "45"
  val aNumber       = aNumberString.toInt
  println("prepending" +: aNumberString :+ "appending")
  println(str.reverse)
  println(str.take(2)) //He

  // Scala Specifics : String Interpolator

  //S-Interpolator:
  val name     = "David"
  val age      = 12
  val greeting = s"Hello, my name is $name and I'm $age year(s) old"
  println(greeting)

  //F-Interpolator: - Can check for type correctness
  val speed = 1.2f
  val myth  = f"$name can eat $speed%2.2f" // Means 2 chars total minimum and 2 decimal precision
  println(myth)

  //Raw-Interpolator:
  println(raw"This is a \n newline")
  val escaped = "\n"
  println(raw"$escaped Hi")
}
