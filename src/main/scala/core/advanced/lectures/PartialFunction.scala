object Advance extends App {
  
  // With anything that this partial function doesn't handle it'll throw
  val partialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 65
    case 5 => 999
  } 

  // Is equivalent to 
  val p = (x: Int) => x match {
    case 1 => 42
    case 2 => 65
    case 5 => 999
  }

  // We can turn it into a function
  val function: (Int => Int) = partialFunction

  // partialFunction(0) // throw a MatchError

  // We can also lift a function
  // Turning a partial to a total function
  val lifted = partialFunction.lift
  lifted(2) // Some(65)
  lifted(1000) // None

  // orElse to chain PF
  val pfChain = partialFunction.orElse[Int, Int] {
    case 60 => 9000
  }
}
