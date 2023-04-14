import cats.effect.Sync

// This pattern is used for HKTs
trait Log[F[_]] {
  def info(str: String): F[Unit]
}

object Log {
  // Summoner pattern
  // This is a way to create instances of Log
  def apply[F[_]: Log]: Log[F] =
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
  def apply[A: Loggable]: Loggable[A] =
    implicitly[Loggable[A]]

  implicit val forString: Loggable[String] =
    new Loggable[String] {
      def log(a: String): String =
        s"String: $a"
    }
}
