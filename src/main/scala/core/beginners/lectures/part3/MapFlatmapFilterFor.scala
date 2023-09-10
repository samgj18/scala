package core.beginners.lectures.part3

object MapFlatmapFilterFor extends App {
  val list = List(1, 2, 3)
  print(list.head)
  print(list.tail)

  println(list.map(_ + 1))

  println(list.filter(_ % 2 == 0))

  val toPair = (x: Int) => List(x, x + 1)
  println(list.flatMap(toPair))

  // Print all combinations between two lists

  val numbers = List(1, 2, 3, 4)
  val chars   = List('a', 'b', 'c', 'd')
  val colors  = List("Black", "White")

  // Functional Iteration
  val combinations = numbers.flatMap(n => chars.map(c => c + "-" + n))

  val combinationOfThree = numbers.flatMap(n => chars.flatMap(c => colors.map(color => c + "-" + n + "-" + color)))

  println(combinations)
  println(combinationOfThree)

  // Function foreach
  list.foreach(println)

  // For-comprehensions

  val forCombinations = for {
    n <- numbers if n % 2 == 0 // This is called a guard
    c     <- chars
    color <- colors
  } yield c + "" + n + "-" + color

  for {
    n <- numbers
  } println(n)

  println(forCombinations)

  // Syntax overload

  list.map { x =>
    x * 2
  }

  /** MyGenericList supports for comprehensions In order to accept for comprehensions the big three must have a special
    * signature map(f: A => B): MyList[B] filter(p: A => Boolean): MyList[A] flatMap(f: A => MyList[B]): MyList[B]
    * Answer: Yes A small collection of at most one element - Maybe[+T]
    *   - map, flatMap, filter
    */

}
