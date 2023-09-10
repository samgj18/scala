package core.beginners.lectures.part1

object ValuesVariablesTypes extends App {
  val x: Int = 42
  // VALS ARE IMMUTABLE, NEVER USE VARS

  // COMPILER CAN INFER TYPES
  val aString: String   = "Hello"
  val aBoolean: Boolean = true
  val aChar: Char       = 'a'
  val anInt: Int        = x
  val aShort: Short     = 12345
  val aLong: Long       = 1234567891011121314L
  val aFloat: Float     = 2.0f
  val aDouble: Double   = 2.0

  // Variables, can be reassigned
  var aVariable: Int = 4

  aVariable = 12 //Side effects

}
