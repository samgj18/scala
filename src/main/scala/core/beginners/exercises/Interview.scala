package exercises

import exercises.AbstractClassVsTraits.RList.{RCons, REmpty}

import scala.annotation.tailrec
import javax.lang.model.element.Element

/** Implement Option
  *
  * @tparam T:
  *   Generic parameter
  */
abstract class Maybe[+T] {
  def map[S](f: T => S): Maybe[S]
  def flatMap[S](f: T => Maybe[S]): Maybe[S]
  def filter(f: T => Boolean): Maybe[T]
}

case object MaybeNot         extends Maybe[Nothing] {
  // No need to implement myself: Can be implemented with flatMap and Pure
  def map[S](f: Nothing => S): Maybe[S] = MaybeNot

  def flatMap[S](f: Nothing => Maybe[S]): Maybe[S] = MaybeNot

  def filter(f: Nothing => Boolean): Maybe[Nothing] = MaybeNot
}

case class Just[T](value: T) extends Maybe[T]       {
  def map[S](f: T => S): Maybe[S] = Just(f(value))

  def flatMap[S](f: T => Maybe[S]): Maybe[S] = f(value)

  def filter(f: T => Boolean): Maybe[T] =
    if (f(value)) this else MaybeNot
}

/** Implement Either
  */

abstract class ThisOrThat[+T] {
  def map[S](f: T => S): Maybe[S]
  def flatMap[S](f: T => Maybe[S]): Maybe[S]
  def filter(f: T => Boolean): Maybe[T]
}

/** If we de-sugar a for comprehension what are we left with? By doing a desugar of a for comprehension we will end up
  * with n - 1 flatMaps and a final map
  */
object For {
  val numbers = List(1, 2, 3, 4)
  val chars   = List('a', 'b', 'c', 'd')
  val colors  = List("Black", "White")

  val combineDesugar = numbers.flatMap(n => chars.flatMap(c => colors.map(color => c + "-" + n + "-" + color)))

  val sugarWithFor = for {
    n <- numbers if n % 2 == 0 // This is called a guard
    c     <- chars
    color <- colors
  } yield c + "" + n + "-" + color
}

/** What is a sealed trait and what advantages can programs derive from its use? Sealed provides exhaustive checking for
  * our application. Exhaustive checking allows to check that all members of a sealed trait must be declared in the same
  * file as of the source file. Exhaustive checking is mostly used in type / pattern matching in scala
  */

/** What is the difference between trait and abstract class? Trait equivalent to interfaces in Java
  */

object AbstractClassVsTraits {
  abstract class Person {
    def discussWith(another: Person): String
    val canDrive: Boolean = true
  }

  trait PersonTrait {
    def discussWith(another: Person): String
    val canDrive: Boolean = true
  }

  /*
   Similarities:
   * They can't be instantiated on their own
   * May have abstract / non-abstract fields/methods
   */

  class Adult(val name: String) extends Person {
    override def discussWith(another: Person): String =
      s"Indeed, $another, Kant was a cool guy"
  }

  // If you extend a SINGLE type, abstract classes and traits have little difference

  /** Differences: Can extend a SINGLE abstract class Can inherit from MULTIPLE traits Abstract classes can take
    * constructor arguments: UNTIL SCALA 3
    */
  abstract class Pet(name: String)
  trait PetTrait

  /** For good practice we represent "things" as classes "behaviours" as traits
    */

  sealed trait RList[+A] {
    def head: A =
      this match {
        case RList.RCons(head, _) => head
        case RList.REmpty         => throw new NoSuchElementException
      }

    def tail: RList[A] =
      this match {
        case RList.RCons(_, tail) => tail
        case RList.REmpty         => throw new NoSuchElementException
      }

    def map[B](f: A => B): RList[B] =
      this match {
        case RList.RCons(head, tail) =>
          @tailrec
          def apply(remaining: RList[A], acc: RList[B]): RList[B] = {
            if (remaining.isEmpty) acc
            else apply(remaining.tail, f(remaining.head) :: acc)
          }
          apply(head :: tail, REmpty)

        case RList.REmpty => REmpty
      }

    def ++[B >: A](list: RList[B]): RList[B] =
      this match {
        case RList.RCons(h, t) =>
          @tailrec
          def apply(remaining: RList[A], acc: RList[B]): RList[B] = {
            if (remaining.isEmpty) acc
            else apply(remaining.tail, remaining.head :: acc)
          }

          apply(this.reverse, REmpty)
        case RList.REmpty      => list
      }

    def foldLeft[B](zero: B)(f: (B, A) => B): B = {
      @tailrec
      def go(rem: RList[A], acc: B): B =
        rem match {
          case RCons(h, t)  => go(t, f(acc, h))
          case RList.REmpty => zero
        }
      go(this, zero)
    }

    def ::[B >: A](elem: B): RList[B] =
      this match {
        case RList.RCons(h, t) => RCons(elem, this)
        case RList.REmpty      => RCons(elem, REmpty)
      }

    def flatMap[B](f: A => RList[B]): RList[B] =
      this match {
        case RCons(h, t)  =>
          @tailrec
          def apply(remaining: RList[A], acc: RList[B]): RList[B] = {
            if (remaining.isEmpty) acc
            else apply(remaining.tail, f(remaining.head) ++ acc)
          }
          apply(this, REmpty)
        case RList.REmpty => REmpty
      }
    def filter(f: A => Boolean): RList[A]      =
      this match {
        case RCons(h, t)  =>
          @tailrec
          def apply(remaining: RList[A], acc: RList[A]): RList[A] = {
            if (f(remaining.head)) remaining.head :: acc
            else apply(remaining.tail, acc)
          }

          apply(this, REmpty)
        case RList.REmpty => REmpty
      }

    def isEmpty: Boolean =
      this match {
        case RCons(h, t)  => false
        case RList.REmpty => true
      }

    def reverse: RList[A] = {
      @tailrec
      def apply(
          remaining: RList[A],
          acc: RList[A]
      ): RList[A] = {
        if (remaining.isEmpty) acc
        else apply(remaining.tail, remaining.head :: acc)
      }

      apply(this, REmpty)
    }
  }
  object RList           {
    final case class RCons[A](h: A, t: RList[A]) extends RList[A]
    case object REmpty                           extends RList[Nothing]
  }
}

object Interview extends App {}
