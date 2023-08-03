package lectures.part0

/** # Stack Data Structure
  *
  * A `Stack` is a `linear data structure` that follows the principle <b>Last In First Out</b>[LIFO]. This means that
  * the last inserted element is the first one to be removed.
  *
  * A good representation of a Stack is a pile of plates:
  *
  * ------------
  * ------------
  * ------------
  * ------------
  * ------------
  *
  * # How it works?
  *
  *   - A pointer called TOP is used to keep track of the top element in the stack.
  *   - When initializing the stack, we set its value to -1 so that we can check if the stack is empty by comparing TOP
  * == -1.
  *   - On pushing an element, we increase the value of TOP and place the new element in the position pointed to by TOP.
  *   - On popping an element, we return the element pointed to by TOP and reduce its value.
  *   - Before pushing, we check if the stack is already full
  *   - Before popping, we check if the stack is already empty
  *
  * Here you add a plate on top OR remove the top plate.
  *
  * # Operations
  *
  * A Stack has five basic operations:
  *
  *   - `pop`: The act of removing a "plate" at the top
  *   - `push`: The act of addinga "plate" at the top
  *   - `isEmpty`: Check if the Stack is empty
  *   - `isFull`: Check if the Stack is full (depending whether if it's unbounded or not)
  *   - `peek`: Get the value of the top element without removing it
  *
  * Beware that everything under lectures part0 are solely for learning purposes and they are indeed very naive
  * implementations of common Data Structures.
  *
  * # Time Complexity
  *
  * Theoretically `pop` and `push` should take constant time O(1), but in Scala using a list this is not true as there's
  * no `insertAtIndex` method or a Java-ish:
  *
  * var arr = []; arr[++top] = elem
  *
  * # Usage
  *
  *   - To reverse a word - Put all the letters in a stack and pop them out. Because of the LIFO order of stack, you
  *     will get the letters in reverse order.
  *   - In compilers - Compilers use the stack to calculate the value of expressions like 2 + 4 / 5 * (7 - 9) by
  *     converting the expression to prefix or postfix form.
  *   - In browsers - The back button in a browser saves all the URLs you have visited previously in a stack. Each time
  *     you visit a new page, it is added on top of the stack. When you press the back button, the current URL is
  *     removed from the stack, and the previous URL is accessed.
  */

// Let's create a non thread safe bounded Stack
class Stack[A](size: Int) {
  // A pointer called TOP is used to keep track of the top element in the stack.
  // When initializing the stack, we set its value to -1 so that we can check if the stack is empty by comparing TOP == -1.
  private var top = -1
  private var arr = List.empty[A]

  def pop: A = {
    if (isEmpty) throw new NoSuchElementException("Stack is empty")
    else {
      val head = arr(top)
      top = top - 1
      arr = arr.init
      head
    }
  }

  def push(elem: A): Unit = {
    // On pushing an element, we increase the value of TOP and place the new element in the position pointed to by TOP.
    if (isFull) throw new StackOverflowError("Stack is full")
    else {
      top = top + 1
      arr = arr :+ elem
    }
  }

  def isEmpty: Boolean = arr.size == 0

  def isFull: Boolean = arr.size == size

  def peek: A = arr(top)
}

object Stack extends App {
  val stack = new Stack[String](2)
  stack.push("Shinny new element")
  stack.push("Shinny new element number 2")
  assert(stack.isFull)
  assert(stack.peek == "Shinny new element number 2")
  assert(stack.pop == "Shinny new element number 2")
  assert(stack.pop == "Shinny new element")
  assert(stack.isEmpty)
}
