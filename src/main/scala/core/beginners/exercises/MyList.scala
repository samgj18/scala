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