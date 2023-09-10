package core.beginners.lectures.part0

/** # Queue Data Structure
  *
  * A `Queue` is a `linear data structure` that follows the principle <b>First In First Out</b> [FIFO].This means that
  * the first inserted element is the
  *
  * A good representation of a Queue is a queue in the bank, first who get the ticket is the first to get called
  *
  * ```
  * -o   o   o   o   o
  * /|\ /|\ /|\ /|\ /|\
  * / \ / \ / \ / \ / \
  * ```
  *
  * # How it works?
  *
  *   - two pointers FRONT and REAR
  *   - FRONT track the first element of the queue
  *   - REAR track the last element of the queue
  *   - initially, set value of FRONT and REAR to -1
  *
  * # Operations
  *
  * A queue is an object (an abstract data structure - ADT) that allows the following operations:
  *
  *   - Enqueue: Add an element to the end of the queue
  *   - Dequeue: Remove an element from the front of the queue
  *   - IsEmpty: Check if the queue is empty
  *   - IsFull: Check if the queue is full
  *   - Peek: Get the value of the front of the queue without removing it
  */

class Queue[A](size: Int) {

  private var rear  = -1
  private var front = -1
  // Might as well be a mutable one
  private var arr   = List.empty[A]

  def isFull: Boolean = size == arr.size

  def isEmpty: Boolean = arr.size == 0

  def enqueue(element: A): Unit = {
    if (isFull) throw new IllegalStateException("Queue is full")
    else {
      if (rear == -1 && front == -1) { // If first element
        front = 0
      }

      rear = rear + 1
      // Ugly workaround
      arr = arr.patch(rear, List(element), 0)
    }
  }

  def dequeue: A = {
    if (isEmpty) throw new NoSuchElementException("Queue is empty")
    else {
      val first = arr.head
      if (arr.size == 1) { // Check if is the last element
        arr = List.empty[A]
        front = -1
        rear = -1
      } else {
        front = front + 1
        arr = arr.tail
      }
      first
    }
  }

  def length: Int = arr.size

}

object Queue extends App {
  val queue = new Queue[String](5)
  queue.enqueue("Me")
  queue.enqueue("You")
  queue.enqueue("Him")
  queue.enqueue("They")
  queue.enqueue("We")

  assert(queue.length == 5)

  assert(queue.dequeue == "Me")
  assert(queue.dequeue == "You")
  queue.enqueue("You all")
  assert(queue.dequeue == "Him")
  assert(queue.dequeue == "They")
  assert(queue.dequeue == "We")
  assert(queue.dequeue == "You all")

}
