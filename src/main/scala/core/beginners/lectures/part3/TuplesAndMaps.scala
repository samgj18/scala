package lectures.part3

import scala.annotation.tailrec

object TuplesAndMaps extends App {
  // Tuples: Finite ordered like "List"
  val aTuple = Tuple2(2, "Hello Scala") // Tuple[Int, String] = (Int, String)

  println(aTuple._1) // 2
  println(aTuple.copy(_2 = "Bye Java"))
  println(aTuple.swap) // ("Hello Scala", 2)

  // Maps - keys -> values

  val aMap: Map[String, Int] = Map()
  val phoneBook =
    Map(("Jim", 555), "Daniel" -> 789, "JIM" -> 291)
      .withDefaultValue(-1) // "Daniel" -> 789 == ("Daniel", 789)

  println(phoneBook)

  // Map ops
  println(phoneBook.contains("Jim"))
  println(phoneBook("Jim"))
  println(phoneBook("No existing key"))

  val pairing = "Mary" -> 678
  val newPhoneBook = phoneBook + pairing
  println(newPhoneBook)

  //Functional on Map: map, flatMap, filter
  println(phoneBook.map(pair => pair._1.toLowerCase -> pair._2))

  // FilterKeys
  println(phoneBook.filterKeys(_.startsWith("J")))

  // MapValues
  println(phoneBook.mapValues(_ * 10))

  // Conversions
  println(phoneBook.toList)
  println(List(("Jim", 555), ("Daniel", 789)).toMap)
  val names = List("Bob", "James", "Angela", "Daniel", "Jim")
  println(names.groupBy(_.charAt(0)))

  /**
    * 1. What would happen if I had two original entries "Jim" -> 555 and "JIM" -> 900?
    *         Depending who I put at the end of the map that'll remain as the last value
    * 2. Overly simplified social network based on maps
    *    Person: String
    *       - add person to the network
    *       - remove
    *       - friend (mutual)
    *       - unfriend
    *
    *       - numbers of friends
    *       - person with most friends
    *       - how many people have no friends
    *       - is there a social connection between two people
    */

  case class Person(name: String)

  def add(
      network: Map[Person, List[Person]],
      toAdd: Person
  ): Map[Person, List[Person]] =
    network + (toAdd -> List())

  def friend(
      network: Map[Person, Set[Person]],
      connectionOne: Person,
      connectionTwo: Person
  ): Map[Person, Set[Person]] = {
    val friendsOfConnectionOneMaybe = network.get(connectionOne)
    val friendsOfConnectionTwoMaybe = network.get(connectionTwo)

    for {
      friendsOfConnectionOne <- friendsOfConnectionOneMaybe
      friendsOfConnectionTwo <- friendsOfConnectionTwoMaybe

    } yield {
      network +
        (connectionOne -> (friendsOfConnectionOne + connectionTwo)) +
        (connectionTwo -> (friendsOfConnectionTwo + connectionOne))
    }

  }.get // Just for studying purposes. Never do get in production apps

  def unfriend(
      network: Map[Person, Set[Person]],
      connectionOne: Person,
      connectionTwo: Person
  ): Map[Person, Set[Person]] = {
    val friendsOfConnectionOneMaybe = network.get(connectionOne)
    val friendsOfConnectionTwoMaybe = network.get(connectionTwo)

    for {
      friendsOfConnectionOne <- friendsOfConnectionOneMaybe
      friendsOfConnectionTwo <- friendsOfConnectionTwoMaybe

    } yield {
      network +
        (connectionOne -> (friendsOfConnectionOne - connectionTwo)) +
        (connectionTwo -> (friendsOfConnectionTwo - connectionOne))
    }
  }.get // Just for studying purposes. Never do get in production apps

  def remove(
      network: Map[Person, Set[Person]],
      person: Person
  ): Map[Person, Set[Person]] = {
    @tailrec
    def removeFriends(
        friends: Set[Person],
        acc: Map[Person, Set[Person]]
    ): Map[Person, Set[Person]] =
      if (friends.isEmpty) acc
      else removeFriends(friends.tail, unfriend(acc, person, friends.head))
    val unfriended = removeFriends(network(person), network)
    unfriended - person
  }

  def nFriends(network: Map[Person, List[Person]], person: Person): Int =
    if (network.contains(person)) 0
    else network(person).size

  def mostFriends(network: Map[Person, List[Person]]): Person =
    network.maxBy(pair => pair._2.size)._1

  def nPeopleWithNoFriends(network: Map[Person, List[Person]]): Int =
    network.count(_._2.isEmpty)

  def socialConnection(
      network: Map[Person, Set[Person]],
      connectionOne: Person,
      connectionTwo: Person
  ): Boolean = {
    def bfs(
        target: Person,
        consideredPeople: Set[Person],
        discoveredPeople: Set[Person]
    ): Boolean = {
      if (discoveredPeople.isEmpty) false
      else {
        val person = discoveredPeople.head
        if (person == target) true
        else if (consideredPeople.contains(person))
          bfs(target, consideredPeople, discoveredPeople.tail)
        else
          bfs(
            target,
            consideredPeople + person,
            discoveredPeople.tail ++ network(person)
          )
      }
    }

    bfs(connectionTwo, Set(), network(connectionTwo) + connectionTwo)
  }
}
