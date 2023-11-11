package core.beginners.lectures.part0

/** An Array is a contiguous memory allocation for storing values which contains a fixed number of bytes. [ ] <- Array [
  * 0 | 1 | 2 | 3 ] <- Index
  */
object MyArray extends App {
  // Technically, when accessing an element of an array what is happening
  // in general is that the following formula is being applied:
  //
  // memory_address + width_of_element * (index - first_index)
  // Remember that index - first_index is the offset of the element
  val numbers = Array(1, 2, 3, 4, 5)
  println(numbers(3)) // 4

}
