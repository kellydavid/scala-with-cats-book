package com.dvdkly.scalawithcats.typeclasses

object ShowExercise {

  import cats.Show
  import cats.instances.int._
  import cats.instances.string._

  val showInt: Show[Int] = Show.apply[Int]
  val showString: Show[String] = Show.apply[String]

  val intAsString: String = showInt.show(3)
  val stringAsString: String = showString.show("str")

}

object ShowWithSyntaxExercise {

  import cats.syntax.show._
  import cats.instances.int._
  import cats.instances.string._

  val intAsString: String = 3.show
  val stringAsString: String = "str".show

}

object CatsImportsGuide {
//  import cats.syntax.show._import cats.implicits._ // imports all of the std type class instances and all of the syntax in one go.
}

object ShowCustomInstances {
  import java.util.Date

  import cats.Show

  implicit val dateShow: Show[Date] = (date: Date) => s"${date.getTime}ms since the epoch."
}

object ShowConvenienceCustomInstances {
  import java.util.Date

  import cats.Show

  implicit val dateShow: Show[Date] = Show.show(date => s"${date.getTime}ms since the epoch.")
}

object ExerciseCatShow extends App {
  import cats.Show
  import cats.syntax.show._

  final case class Cat(name: String, age: Int, color: String)
  object Cat {
    implicit val showInstance: Show[Cat] = Show.show(cat => s"${cat.name} is a ${cat.age} year-old ${cat.color} cat.")
  }

  println(Cat("Tom", 3, "Ginger").show)
}