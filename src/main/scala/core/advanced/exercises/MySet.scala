package core.advanced.exercises

import cats.instances.tailRec
import scala.annotation.tailrec

trait MySet[A] extends (A => Boolean) {
  def apply(element: A): Boolean = contains(element)
  def contains(elem: A): Boolean
  def +(elem: A): MySet[A]
  def ++(anotherSet: MySet[A]): MySet[A]

  def map[B](f: A => B): MySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B]
  def filter(predicate: A => Boolean): MySet[A]

  def -(elem: A): MySet[A]
  def --(anotherSet: MySet[A]): MySet[A]
  def &(anotherSet: MySet[A]): MySet[A]

  def unary_! : MySet[A]
}

class EmptySet[A] extends MySet[A] { self =>
  override def ++(anotherSet: MySet[A]): MySet[A] = anotherSet

  override def filter(predicate: A => Boolean): MySet[A] = self

  override def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]

  override def map[B](f: A => B): MySet[B] = new EmptySet[B]

  override def +(elem: A): MySet[A] = new NonEmptySet[A](elem, self)

  override def contains(elem: A): Boolean = false

  override def -(elem: A): MySet[A] = self

  override def --(anotherSet: MySet[A]): MySet[A] = self

  override def &(anotherSet: MySet[A]): MySet[A] = self

  override def unary_! : MySet[A] = new AllInclusiveSet[A]

}

class AllInclusiveSet[A] extends MySet[A] {

  override def contains(elem: A): Boolean = true
  override def +(elem: A): MySet[A] = this
  override def ++(anotherSet: MySet[A]): MySet[A] = this

  override def map[B](f: A => B): MySet[B] = ???
  override def flatMap[B](f: A => MySet[B]): MySet[B] = ???
  override def filter(predicate: A => Boolean): MySet[A] = ???

  override def -(elem: A): MySet[A] = ???
  override def --(anotherSet: MySet[A]): MySet[A] = ???
  override def &(anotherSet: MySet[A]): MySet[A] = ???

  override def unary_! : MySet[A] = ???

}

class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A] { self =>
  override def contains(elem: A): Boolean = elem == head || tail.contains(elem)

  override def ++(anotherSet: MySet[A]): MySet[A] = tail ++ anotherSet + head

  override def filter(predicate: A => Boolean): MySet[A] = {
    val filtered = tail filter predicate
    if (predicate(head)) filtered + head
    else filtered
  }

  override def flatMap[B](f: A => MySet[B]): MySet[B] =
    (tail flatMap f) ++ f(head)

  override def map[B](f: A => B): MySet[B] = (tail map f) + f(head)

  override def +(elem: A): MySet[A] =
    if (self.contains(elem)) self else new NonEmptySet[A](elem, self)

  override def -(elem: A): MySet[A] = self.filter(_ != elem)

  override def --(anotherSet: MySet[A]): MySet[A] =
    self.filter(x => !anotherSet(x))

  override def &(anotherSet: MySet[A]): MySet[A] = self.filter(anotherSet)

  override def unary_! : MySet[A] = filter(self)
}

object MySet {
  def apply[A](values: A*): MySet[A] = {
    @tailrec
    def make(seq: Seq[A], acc: MySet[A]): MySet[A] = {
      if (seq.isEmpty) acc
      else make(seq.tail, acc + seq.head)
    }

    make(values.toSeq, new EmptySet())
  }
}

object Playground extends App {
  val s1 = MySet(1, 2, 3, 4)

  val s2 = MySet(1, 2, 3, 4)

  (s1 - 1 - 2 - 3 - 4) map println
}
