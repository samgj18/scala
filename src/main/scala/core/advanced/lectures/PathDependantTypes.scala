package core.advanced.lectures

import scala.concurrent.duration.Duration

// 1. First use of Path Dependant Types
// So that we can have various output types for our functions
// or classes.
//
// 2. Second use of Path Dependant Types
// So that we can have different types of `States` for our
// `Boxes`, this will make sense in a while.
//
object PathDependantTypes extends App {
  // This is NOT a path dependant type
  trait Box[+T] {
    def value: T
  }

  // This is a path dependant type
  // Allows to "hide" the information of the type
  trait PathDependantBox { self =>
    type T
    def value: T

    def tupleBox(that: PathDependantBox): PathDependantBox = {
      new PathDependantBox {
        override type T = (self.T, that.T)
        override def value: T = (self.value, that.value)
      }
    }
  }

  // Some examples:

  // CountDownLatches
  // A latch is a synchronization mechanism that can be used to
  // ensure that a task being executed by one thread completes
  // before a task being executed by another thread starts.
  //
  trait CountDownLatch {
    type State
    // Protected because we don't want the user to be able to
    // change the state of the latch.
    protected def updateState: State => State

    def countDown(): Unit
    def await(): Unit
  }

  // Schedule
  trait Schedule {
    type State
    val initial: State

    def next(state: State): (State, Int, Decision)
  }

  sealed trait Decision
  final case class Continue(delay: Duration) extends Decision
  case object Stop                           extends Decision

  object PathDependantBox {
    type Aux[A0] = PathDependantBox { type T = A0 }

    def applyWithType[A0](v: A0): Aux[A0] = new PathDependantBox {
      override type T = A0
      override def value: T = v
    }

    def apply[A0](v: A0): PathDependantBox = new PathDependantBox {
      override type T = A0
      override def value: T = v
    }
  }

  val box: PathDependantBox         = PathDependantBox(42)
  val box2: PathDependantBox        = PathDependantBox("Scala")
  // val sum  = box.value + box2.value // This won't compile because the
  // compiler doesn't know the type of the value of the boxes.
  val boxes: List[PathDependantBox] = List(box, box2)
  val tupleBox: PathDependantBox    = box.tupleBox(box2)

  val boxWithType: PathDependantBox.Aux[Int]     = PathDependantBox.applyWithType(42)
  val boxWithType2: PathDependantBox.Aux[String] =
    PathDependantBox.applyWithType("Scala")

  val sum = boxWithType.value + boxWithType2.value

  // Unifying zippable items
  trait DomainError

  trait InvalidInput extends DomainError
  trait InvalidState extends DomainError

  val either: Either[InvalidInput, InvalidState]                                              = ???
  val either2: Either[InvalidInput, Either[InvalidState, InvalidState]]                       = ???
  val either3: Either[Either[InvalidInput, InvalidState], InvalidState]                       = ???
  ???
  val either4: Either[Either[InvalidInput, InvalidState], Either[InvalidState, InvalidState]] =
    ???

  trait Union[-In] {
    type Out

    def unify(in: In): Out
  }

  // This is first in the priority chain
  object Union extends UnionLowPriority1 {
    type Aux[In, Out0] = Union[In] { type Out = Out0 }

    implicit def UnionEitherBoth[A]: Union.Aux[Either[Either[A, A], Either[A, A]], A] =
      new Union[Either[Either[A, A], Either[A, A]]] {

        override def unify(in: Either[Either[A, A], Either[A, A]]): A = in.fold(_.merge, _.merge)

        override type Out = A
      }

  }

  // This is second in the priority chain
  trait UnionLowPriority1 extends UnionLowPriority2 {

    implicit def UnionEitherRight[A]: Union.Aux[Either[A, Either[A, A]], A] =
      new Union[Either[A, Either[A, A]]] {

        override def unify(in: Either[A, Either[A, A]]): A = in.fold(identity, _.merge)

        override type Out = A
      }

    implicit def UnionEitherLeft[A]: Union.Aux[Either[Either[A, A], A], A]  =
      new Union[Either[Either[A, A], A]] {

        override def unify(in: Either[Either[A, A], A]): A = in.fold(_.merge, identity)

        override type Out = A
      }
  }

  // This is third in the priority chain
  trait UnionLowPriority2 {

    implicit def UnionEither[A]: Union.Aux[Either[A, A], A] = new Union[Either[A, A]] {

      override def unify(in: Either[A, A]): A = in.merge

      override type Out = A
    }
  }

  def unify[In](in: In)(implicit union: Union[In]): union.Out = ???

  val result1: DomainError = unify(either)
  val result2: DomainError = unify(either2)
  val result3: DomainError = unify(either3)
  val result4: DomainError = unify(either4)

  // There are limitations to make this inductively that's why we have to write all specific
  // priorities.
}
