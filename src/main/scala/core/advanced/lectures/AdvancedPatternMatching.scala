package core.advanced.lectures

object AdvancedPatternMatching extends App {
  val numbers = List(1)
  val description = numbers match {
    case head :: Nil => println(s"the only element is $head")
    case _           =>
  }

  /*
    - constants
    - wildcards
    - case classes
    - tuples
    - some special magic like above
   */

  class Person(val name: String, val age: Int)
  object Person {
    def unapply(person: Person): Option[(String, Int)] =
      if (person.age < 21) None
      else Some((person.name, person.age))
  }

  val bob = new Person("Bob", 25)
  val greeting = bob match {
    case Person(n, a) => s"Hi, my name is $n and I am $a years old"
  }

  val n: Int = 45
  val mathProperty = n match {
    case x if x % 2 == 0 => "even"
    case _               => "odd"
  }

  object even {
    def unapply(arg: Int): Boolean =
      arg % 2 == 0
  }

  object singleDigit {
    def unapply(arg: Int): Option[Boolean] =
      if (arg > -10 && arg < 10) Some(true) else None
  }

  val n1 = 45
  val mathProperty1 = n1 match {
    case even()         => "even"
    case singleDigit(_) => "single digit"
    case _              => "odd"
  }

  // infix patterns
  case class Or[A, B](a: A, b: B) // Either
  val either = Or(2, "two")
  val humanDescription = either match {
    case number Or string => s"$number is written as $string"
  }

  // decomposing sequences
  val vararg = numbers match {
    case List(1, _*) => "starting with 1"
  }

  abstract class MyList[+A] {
    def head: A = ???
    def tail: MyList[A] = ???

  }
  case object Empty extends MyList[Nothing]
  case class Cons[+A](override val head: A, override val tail: MyList[A])
      extends MyList[A]

  object MyList {
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] =
      if (list == Empty) Some(Seq.empty)
      else unapplySeq(list.tail).map(list.head +: _)
  }

  val myList: MyList[Int] = Cons(1, Cons(2, Cons(3, Empty)))
  val decomposed = myList match {
    case MyList(1, 2, _*) => "starting with 1, 2"
    case _                => "something else"
  }

  // custom return types for unapply
  // isEmpty: Boolean, get: something

  abstract class Wrapper[T] {
    def isEmpty: Boolean
    def get: T
  }

  object PersonWrapper {
    def unapply(person: Person): Wrapper[Person] = new Wrapper[Person] {
      def isEmpty = false
      def get = person
    }
  }

  println(bob match {
    case PersonWrapper(n) => s"This person's name is ${n.name}"
    case _                => "An alien"
  })
}

object TypeClassImpl {
  trait Functor[F[_]] {
    def map[A, B](fa: F[A])(f: A => B): F[B]
  }

  object Functor {
    def apply[F[_]: Functor]: Functor[F] = implicitly[Functor[F]]

    implicit val listFunctor: Functor[List] = new Functor[List] {
      override def map[A, B](fa: List[A])(f: A => B): List[B] = fa.map(f)
    }

    implicit class FunctorSyntax[F[_]: Functor, A](fa: F[A]) {
      def map[B](f: A => B): F[B] = implicitly[Functor[F]].map(fa)(f)
    }
  }

  trait Applicative[F[_]] extends Functor[F] {
    def zipWith[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C]
    def pure[A](a: A): F[A]
  }

  object Applicative {
    def apply[F[_]: Applicative]: Applicative[F] = implicitly[Applicative[F]]

    implicit val listApplicative: Applicative[List] = new Applicative[List] {
      override def map[A, B](fa: List[A])(f: A => B): List[B] = fa.map(f)

      override def zipWith[A, B, C](fa: List[A], fb: List[B])(
          f: (A, B) => C
      ): List[C] =
        fa.zip(fb).map(f.tupled)

      override def pure[A](a: A): List[A] = List(a)
    }

    implicit class ApplicativeSyntax[F[_]: Applicative, A](fa: F[A]) {
      def zipWith[B, C](fb: F[B])(f: (A, B) => C): F[C] =
        implicitly[Applicative[F]].zipWith(fa, fb)(f)

      def pure[B](a: B): F[B] = implicitly[Applicative[F]].pure(a)
    }
  }

}

object Implicits {
  implicit class IntEvenSyntax(i: Int) {
    def isEven: Boolean = i % 2 == 0
  }

}
