package exercises

abstract class MyList {
  /*
    head = First element of the list
    tail = Remainder of the list
    isEmpty = Is this list empty
    add(int) => New list with this element added
    toString => A string representation of the list
   */

  def head: Int
  def tail: MyList
  def isEmpty: Boolean
  def add(element: Int): MyList
  def printElements: String
  // Polymorphic call
  override def toString: String = "{" + printElements + "}"
}

object Empty extends MyList {
  override def head: Int = throw new NoSuchElementException

  override def tail: MyList = throw new NoSuchElementException

  override def isEmpty: Boolean = true

  override def add(element: Int): MyList = new Cons(element, Empty)

  override def printElements: String = ""
}

class Cons(h: Int, t: MyList) extends MyList {
  override def head: Int = h

  override def tail: MyList = t

  override def isEmpty: Boolean = false

  override def add(element: Int): MyList = new Cons(element, this)

  override def printElements: String =
    if (t.isEmpty) "" + h
    else h + " " + t.printElements
}

object ListTest extends App {
  val list = new Cons(1, new Cons(2, new Cons(3, Empty)))

  println(list.toString)
}

// Alternative not OO implemetation

sealed trait NotOOList[+A] {
  def head: A =
    this match {
      case NotOOList.::(head, _) => head
      case NotOOList.Empty       => throw new NoSuchElementException
    }
  def tail: NotOOList[A] =
    this match {
      case NotOOList.::(_, tail) => tail
      case NotOOList.Empty       => throw new NoSuchElementException
    }
  def map[B](f: A => B): NotOOList[B]
  def flatMap[B](f: A => NotOOList[B]): NotOOList[B]
  def filter[B](f: A => Boolean): NotOOList[B]
  def isEmpty: Boolean
}

object NotOOList {
  final case class ::[A](override val head: A, override val tail: NotOOList[A])
      extends NotOOList[A] {
    override def map[B](f: A => B): NotOOList[B] = ???

    override def flatMap[B](f: A => NotOOList[B]): NotOOList[B] = ???

    override def filter[B](f: A => Boolean): NotOOList[B] = ???

    override def isEmpty: Boolean = false
  }
  case object Empty extends NotOOList[Nothing] {
    override def head: Nothing = throw new NoSuchElementException

    override def tail: NotOOList[Nothing] = throw new NoSuchElementException

    override def map[B](f: Nothing => B): NotOOList[B] = ???

    override def flatMap[B](f: Nothing => NotOOList[B]): NotOOList[B] = ???

    override def filter[B](f: Nothing => Boolean): NotOOList[B] = ???

    override def isEmpty: Boolean = true
  }
}
