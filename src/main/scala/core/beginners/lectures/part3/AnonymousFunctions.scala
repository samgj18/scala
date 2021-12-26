package lectures.part3

object AnonymousFunctions extends App {
  val doubler = new Function[Int, Int] {
    override def apply(v1: Int): Int = v1 * 2
  }
  // Anonymous Function - Lambda
  val shortedDoubler = (x: Int) => x * 2
  val shortedDoublerExplicit: Int => Int = x => x * 2

  val adder = (x: Int, y: Int) => x + y
  val adderExplicit: (Int, Int) => Int = (x, y) => x + y

  val justDo: () => Int = () => 3

  println(justDo) // Function itself
  println(justDo()) // Actual call

  // Curly braces lambdas
  val stringToInt = { (str: String) => str.toInt }

  // More Syntactic Sugar
  val incrementer: Int => Int = _ + 1 // x => x + 1
  val niceAdder: (Int, Int) => Int = _ + _ // Equivalent to (x, y) => x + y

  /**
    * 1. Go to replace all FunctionX calls with lambdas in GenericMyList
    * 2. Replace superSum from WhatsAFunction as a lambda
    */

  val superSum = (value: Int) => (inner: Int) => inner + value
}
