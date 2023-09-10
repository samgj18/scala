package core.beginners.lectures.part1

object CBNvsCBV extends App {
  def calledByValue(x: Long): Unit = { // Evaluate x before and use it in the entire function
    println("By value " + x)
    println("By value " + x)
  }

  def calledByName(x: => Long): Unit = { // Evaluate every time is called
    println("By name " + x)
    println("By name " + x)
  }

  calledByValue(System.nanoTime())
  calledByName(System.nanoTime())

  def infinite(): Int                     = 1 + infinite()
  def printFirst(x: Int, y: => Int): Unit = println(x)

  //  printFirst(infinite(), 32)  This one will crash
  printFirst(34, infinite()) // This won't crash because of the lazy eval that : => does.
}
