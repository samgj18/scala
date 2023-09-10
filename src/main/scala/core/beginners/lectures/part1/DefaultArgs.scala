package core.beginners.lectures.part1

object DefaultArgs extends App {
  def trFact(n: Int, acc: Int = 1): Int = {
    if (n <= 1) acc
    else trFact(n - 1, n * acc)
  }
  val fact10                            = trFact(10)

  def savePicture(format: String = "jpg", width: Int = 1920, height: Int = 1080): Unit = println("Saving picture")
  /*
    1. Pass every leading argument
    2. Name the arguments
   */
  savePicture(width = 800)
  //Error savePicture(800)

}
