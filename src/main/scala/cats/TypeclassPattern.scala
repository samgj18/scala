package catsstuff

import cats.effect.Sync

// This pattern is used for HKTs
trait Log[F[_]] {
  def info(str: String): F[Unit]
}

object Log {
  // Summoner pattern
  // This is a way to create instances of Log
  def apply[F[_]: Log]: Log[F]                =
    implicitly[Log[F]]

  implicit def forConsole[F[_]: Sync]: Log[F] =
    new Log[F] {
      def info(str: String): F[Unit] =
        Sync[F].delay(println(str))
    }
}

// This pattern is used for ADTs
trait Loggable[A] {
  def log(a: A): String
}

object Loggable {
  // Summoner pattern
  // This is a way to create instances of Loggable
  def apply[A: Loggable]: Loggable[A]      =
    implicitly[Loggable[A]]

  implicit val forString: Loggable[String] =
    new Loggable[String] {
      def log(a: String): String =
        s"String: $a"
    }
}

trait Addable[A] {
  def add(a: A, b: A): A
}

object Addable {
  // We can use the following pattern to create a "summoner" method
  // Summoner Pattern
  def apply[A: Addable]: Addable[A] = implicitly[Addable[A]]

  implicit val intAddable: Addable[Int] = (a: Int, b: Int) => a + b

  implicit val stringAddable: Addable[String] = (a: String, b: String) => a + b

  implicit def listAddable[A]: Addable[List[A]] = (a: List[A], b: List[A]) => a ++ b

  implicit class AddableOps[A: Addable](a: A) {
    def add(b: A): A = Addable[A].add(a, b)
  }
}

object Run extends App {
  import Addable._

  println(Addable[Int].add(1, 2))
  println(Addable[String].add("a", "b"))
  println(Addable[List[Int]].add(List(1, 2), List(3, 4)))

  println(1.add(2))
  println("a".add("b"))
  println(List(1, 2).add(List(3, 4)))
}

object TypeClasses extends App {
  // We do it to add capabilities to existing types
  final case class Person(name: String)

  // 1. Typeclass definition
  trait JSONSerializer[T] {
    def toJSON(t: T): String
  }

  // 2. Create implicit typeclass instances
  implicit object PersonJSONSerializer extends JSONSerializer[Person] {
    def toJSON(person: Person): String =
      s"""
         |{"name": "${person.name}"}
         |""".stripMargin
  }

  implicit object StringJSONSerializer extends JSONSerializer[String] {
    def toJSON(str: String): String =
      s"""
         |{"str": "$str"}
         |""".stripMargin
  }

  implicit object IntJSONSerializer    extends JSONSerializer[Int]    {
    def toJSON(int: Int): String =
      s"""
         |{"int": "$int"}
         |""".stripMargin
  }

  // 3. Offer some API
  def convertToJSON[T](value: T)(implicit
      serializer: JSONSerializer[T]
  ): String =
    serializer.toJSON(value)

  // 4. Extend existing types via extension methods
  object JSONSyntax {
    implicit class JSONSerializableOps[T](value: T)(implicit
        serializer: JSONSerializer[T]
    ) {
      def toJSON: String =
        serializer.toJSON(value)
    }
  }

  // 4. Use API
  import JSONSyntax._

  println(convertToJSON(Person("John")))
  println(Person("John").toJSON)

}
