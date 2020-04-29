package com.dvdkly.scalawithcats.typeclasses

trait Printable[A] {
  def format(value: A): String
}

object PrintableInstances {
  implicit val stringInstance: Printable[String] = (value: String) => s"String: $value"
  implicit val intInstance: Printable[Int] = (value: Int) => s"Number: $value"
}

object Printable {
  implicit def format[A](value: A)(implicit p: Printable[A]): String = p.format(value)
  implicit def print[A](value: A)(implicit p: Printable[A]): Unit = println(p.format(value))
}

object PrintableSyntax {
  implicit class PrintableOps[A](value: A){
    def format(implicit p: Printable[A]): String = Printable.format(value)
    def print(implicit p: Printable[A]): Unit = Printable.print(value)
  }
}

object Exercise extends App {

  import PrintableInstances._
  import PrintableSyntax._

  final case class Cat(name: String, age: Int, color: String)
  object Cat {
    implicit val printableInstance: Printable[Cat] =
      (cat: Cat) => s"${cat.name} is a ${cat.age} year-old ${cat.color} cat."
  }

  "david".print
  33.print
  Cat("Tom", 3, "Ginger").print
}
