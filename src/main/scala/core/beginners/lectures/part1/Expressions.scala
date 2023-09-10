package core.beginners.lectures.part1

object Expressions extends App {
  val x = 1 + 2 // EXPRESSION
  println(x)

  println(1 + 2 * 4)
  // + - * / & | ^ << >> >>> (Right shift with zero extension)

  println(1 == x)
  // == != > >= < <=

  println(!(1 == x))
  // ! && ||

  var aVariable = 2
  aVariable += 3

  // Instructions (DO) vs Expressions (Value)
  val aCondition      = true
  val aConditionValue = if (aCondition) 5 else 3 // IF EXPRESSION

  var i = 0
  while (i < 0) { // Never write this again
    println(
      i += 1
    )
  }

  // EVERYTHING in Scala is an Expression!
  // A Side Effect is an Expression returning Unit

  val aWeirdValue = (aVariable = 3) // Unit === Void
  // Side Effects: println(), whiles, reassigning

  //Code blocks
  val aCodeBlock = {
    val y = 2
    val z = y + 1

    if (z > 2) "Hello" else "GoodBye"
  }

}
