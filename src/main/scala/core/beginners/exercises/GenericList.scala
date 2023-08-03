package exercises

abstract class GenericMyList[+A] {
  /*
    head = First element of the list
    tail = Remainder of the list
    isEmpty = Is this list empty
    add(int) => New list with this element added
   */

  def head: A
  def tail: GenericMyList[A]
  def isEmpty: Boolean
  def add[B >: A](element: B): GenericMyList[B]

  def map[B](transformer: A => B): GenericMyList[B]
  def flatMap[B](
      transformer: A => GenericMyList[B]
  ): GenericMyList[B]
  def filter(predicate: A => Boolean): GenericMyList[A]

  //HOF's
  def foreach(f: A => Unit): Unit
  def sort(f: (A, A) => Int): GenericMyList[A]
  def zipWith[B, C](list: GenericMyList[B], f: (A, B) => C): GenericMyList[C]
  def fold[B](start: B)(operator: (B, A) => B): B

  // Concatenation
  def ++[B >: A](list: GenericMyList[B]): GenericMyList[B]

  // Pretty print
  def printElements: String
  override def toString: String = "[" + printElements + "]"
}

object EmptyGeneric extends GenericMyList[Nothing] {
  override def head: Nothing = throw new NoSuchElementException

  override def tail: GenericMyList[Nothing] = throw new NoSuchElementException

  override def isEmpty: Boolean = true

  override def add[B >: Nothing](element: B): GenericMyList[B] =
    new GenericCons(element, EmptyGeneric)

  override def printElements: String                           = ""

  override def map[B](
      transformer: Nothing => B
  ): GenericMyList[B] = EmptyGeneric

  override def flatMap[B](
      transformer: Nothing => GenericMyList[B]
  ): GenericMyList[B] = EmptyGeneric

  override def filter(predicate: Nothing => Boolean): GenericMyList[Nothing] =
    EmptyGeneric

  override def ++[B >: Nothing](list: GenericMyList[B]): GenericMyList[B]    = list

  override def foreach(f: Nothing => Unit): Unit = ()

  override def sort(f: (Nothing, Nothing) => Int): GenericMyList[Nothing] =
    EmptyGeneric

  override def zipWith[B, C](
      list: GenericMyList[B],
      f: (Nothing, B) => C
  ): GenericMyList[C] = {
    if (!list.isEmpty)
      throw new RuntimeException("Lists do not have the same length")
    else EmptyGeneric
  }

  override def fold[B](start: B)(operator: (B, Nothing) => B): B          = start
}

class GenericCons[+A](h: A, t: GenericMyList[A]) extends GenericMyList[A] {
  override def head: A = h

  override def tail: GenericMyList[A] = t

  override def isEmpty: Boolean = false

  override def add[B >: A](element: B): GenericMyList[B] =
    new GenericCons[B](element, this)

  /*
    Filtering with a single linked list means verifying whether the predicate satisfies the
    head and the tail

    [1, 2, 3].filter(n >= 2)
      = [2, 3].filter(n >= 2)
      = new ConsGeneric(2, [3].filter(n >= 2))
      = new ConsGeneric(2, new ConsGeneric(3, EmptyGeneric.filter(n >= 2)))
      = new ConsGeneric(2, new ConsGeneric(3, EmptyGeneric))
   */
  override def filter(predicate: A => Boolean): GenericMyList[A] =
    if (predicate(h)) new GenericCons(h, t.filter(predicate))
    else t.filter(predicate)

  /*
    [1, 2, 3].map(n * 2)
      = new ConsGeneric(2, [2, 3].map(n * 2))
      = new ConsGeneric(2, new ConsGeneric(4, [3].map(n * 2)))
      = new ConsGeneric(2, new ConsGeneric(4, new ConsGeneric(6, EmptyGeneric.map(n * 2))))
      = new ConsGeneric(2, new ConsGeneric(4, new ConsGeneric(6, EmptyGeneric)))
   */

  override def map[B](transformer: A => B): GenericMyList[B] =
    new GenericCons[B](transformer(h), t.map(transformer))

  override def printElements: String                         =
    if (t.isEmpty) "" + h
    else h + ", " + t.printElements

  /*
    [1, 2] ++ [1, 2, 3]
      = new ConsGeneric(1, [2] ++ [1, 2, 3])
      = new ConsGeneric(1, new ConsGeneric(2, EmptyGeneric ++ [1, 2, 3]))
      = new ConsGeneric(1, new ConsGeneric(2, new ConsGeneric(1, new ConsGeneric(2, new ConsGeneric(3, EmptyGeneric)))))
   */
  def ++[B >: A](list: GenericMyList[B]): GenericMyList[B] =
    new GenericCons(h, t ++ list)

  override def flatMap[B](
      transformer: A => GenericMyList[B]
  ): GenericMyList[B] = transformer(h) ++ t.flatMap(transformer)

  override def foreach(f: A => Unit): Unit                 = {
    f(h)
    t.foreach(f)
  }

  override def sort(f: (A, A) => Int): GenericMyList[A]    = {
    def insert(a: A, sorted: GenericMyList[A]): GenericMyList[A] = {
      if (sorted.isEmpty) new GenericCons(a, EmptyGeneric)
      else if (f(a, sorted.head) <= 0) new GenericCons(a, sorted)
      else new GenericCons(sorted.head, insert(a, sorted.tail))
    }
    insert(h, t.sort(f))
  }

  override def zipWith[B, C](
      list: GenericMyList[B],
      f: (A, B) => C
  ): GenericMyList[C] =
    if (list.isEmpty)
      throw new RuntimeException("Lists do not have the same length")
    else new GenericCons(f(h, list.head), t.zipWith(list.tail, f))

  /*
    [1, 2, 3].fold(0)(+)
    = [2, 3].fold(1)(+)
    = [3].fold(3)(+)
    = [].fold(6)(+)
    = 6
   */
  override def fold[B](start: B)(operator: (B, A) => B): B =
    t.fold(operator(start, head))(operator)

}
/*
    1. Generic trait MyPredicate[-T] with method test(T) => Boolean
    2. Generic trait MyTransformer[-A, B] with method transform(A) => B
    3. MyList:
        - map(transformer) => MyList
        - filter(predicate) => MyList
        - flatMap(transformer from A to MyList[B]) => MyList[B]
 */

trait MyPredicate[-T] { // Function type from T => Boolean
  def test(predicate: T): Boolean
}

trait MyTransformer[-A, B] { // Function type from A => B
  def transform(element: A): B
}

object ListTestGeneric extends App {
  val listOfInt: GenericMyList[Int]        =
    new GenericCons[Int](
      1,
      new GenericCons[Int](2, new GenericCons[Int](3, EmptyGeneric))
    )
  val listOfStrings: GenericMyList[String] =
    new GenericCons[String]("Samuel", EmptyGeneric)

  println(listOfInt.map((element: Int) => element * 2).toString)

  println(listOfInt.filter(_ >= 2).toString)

  println(listOfInt.foreach(println))

  println(listOfInt.sort((x, y) => y - x))

  // This works because of the definitions of our maps, flatMaps and filter
  val combinations = for {
    n      <- listOfInt
    string <- listOfStrings
  } yield n + "-" + string

  println(combinations)
}
