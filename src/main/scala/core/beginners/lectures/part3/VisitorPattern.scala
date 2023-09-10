package core.beginners.lectures.part3

object VisitorPattern extends App {

  trait Expression {
    def accept(visitor: Visitor): Int
  }

  case class Number(value: Int)                                  extends Expression { self =>
    override def accept(visitor: Visitor): Int = visitor.visit(self)
  }

  case class Addition(left: Expression, right: Expression)       extends Expression { self =>
    override def accept(visitor: Visitor): Int = visitor.visit(self)
  }

  case class Multiplication(left: Expression, right: Expression) extends Expression { self =>
    override def accept(visitor: Visitor): Int = visitor.visit(self)
  }

  trait Visitor {
    def visit(number: Number): Int
    def visit(addition: Addition): Int
    def visit(multiplication: Multiplication): Int
  }

  class ExpressionPrinter extends Visitor {

    override def visit(number: Number): Int = number.value

    override def visit(addition: Addition): Int             = {
      val left  = addition.left.accept(this)
      val right = addition.right.accept(this)
      left + right
    }

    override def visit(multiplication: Multiplication): Int = {
      val left  = multiplication.left.accept(this)
      val right = multiplication.right.accept(this)
      left * right
    }
  }

  val expression = new Addition(new Number(2), new Multiplication(new Number(3), new Number(4)))
  val printer    = new ExpressionPrinter
  println(expression.accept(printer))

  // Summary of how to implement the Visitor Pattern:
  //
  // 1. Create a trait for the expression hierarchy [Expression { accept }] -> [Number], [Addition], [Multiplication]
  // 2. Create a trait for the visitor hierarchy [Visitor { visit }] -> [ExpressionPrinter] -> [Number], [Addition], [Multiplication]
  // 3. Implement the accept method in the expression hierarchy [Expression { accept }] -> [Number], [Addition], [Multiplication]
  // 4. Implement the visit method in the visitor hierarchy [Visitor { visit }] -> [ExpressionPrinter] -> [Number], [Addition], [Multiplication]
  //
  // The Visitor Pattern is more powerful than pattern matching in terms of performance. See [https://www.youtube.com/watch?v=n5u7DgFwLGE&t=753s&ab_channel=ScalaDaysConferences]
}
