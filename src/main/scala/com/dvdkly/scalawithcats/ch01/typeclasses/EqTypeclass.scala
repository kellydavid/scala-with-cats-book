package com.dvdkly.scalawithcats.ch01.typeclasses


object ExerciseCompareInts extends App {

  import cats.Eq
  import cats.instances.int._

  val eqInt: Eq[Int] = Eq[Int]

  println(eqInt.eqv(3, 3))
  println(eqInt.eqv(1, 2))
}

object ExerciseCompareOptionInt extends App {

  import cats.instances.int._
  import cats.instances.option._ // for Eq instances
  import cats.syntax.eq._
  import cats.syntax.option._ // for nicer Option syntax



  println(3 === 3)
  println(1 === 2)

  println(3.some === none[Int])
  println(4.some === 4.some)
}

object CompareCustomTypes extends App {
  import java.util.Date

  import cats.Eq
  import cats.syntax.eq._
  import cats.instances.long._

  implicit val dateEq: Eq[Date] = Eq.instance[Date]{
    (date1, date2) => date1.getTime === date2.getTime
  }

  val x = Date.from(java.time.Instant.now())
  val y = Date.from(x.toInstant.plusSeconds(3 * 24 * 60 * 60))

  println(x == x)

  println(x == y)
}

object CatExercise extends App {

  import cats.Eq
  import cats.syntax.eq._
  import cats.instances.int._
  import cats.instances.string._
  import cats.instances.option._
  import cats.syntax.option._

  final case class Cat(name: String, age: Int, color: String)
  object Cat {
    implicit val eq: Eq[Cat] = Eq.instance[Cat]{
      (cat1, cat2) => cat1.name === cat2.name && cat1.age === cat2.age && cat1.color === cat2.color
    }
  }

  val tom: Cat = Cat("Tom", 3, "Ginger")
  val garfield: Cat = Cat("Garfield", 42, "Ginger")

  println(tom === garfield)
  println(tom === tom)

  println(tom.some === garfield.some)
  println(garfield.some === none[Cat])

}