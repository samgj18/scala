package lectures.part2

import playground.Cinderella

import playground.{Cinderella => Princess}

object PackagingAndImports extends App {
  // Package members are accessible by their simple name
  val writer = new Writer("Samuel", "Mastering Scala", 2021)

  // Import the package
  val princess = new Cinderella

  // Without importing -> Fully qualified name

  val notImportedPrincess = new playground.Cinderella

  // Packages are in hierarchy

  // Package object

  println(SPEED_OF_LIGHT)

  val some = new Princess
}
