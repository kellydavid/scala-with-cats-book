package com.dvdkly.scalawithcats.ch02

object MonoidSemigroup {

  /**
   * Definition of a Monoid
   *  - Can *combine* two elements (A, A) => A
   *  - Has an *identity element* of type A
   *
   *  eg. for addition, the identity element is 0
   *  eg. for multiplication the identity element is 1
   *
   *  A monoid must uphold two laws:
   *  - Associativity eg 1 + 2 == 2 + 1
   *  - Identity eg 1 + 0 == 1 , 2 * 1 == 2
   *
   * Integer subtraction is not a monoid because subtraction is not associative.
   *
   * Be careful writing instances of a typeclass as the instance must uphold the laws.
   */

  /**
   * Definition of a Semigroup
   *  - Can *combine* two elements (A, A) => A
   *
   * Both Monoid and Semigroup can combine two elements.
   * However, Semigroup has no identity element!
   *
   * Semigroup instance exists for NonEmptyList but not Monoid
   */
}

object MonoidExercise {

  trait Semigroup[A] {
    def combine(x: A, y: A): A
  }

  trait Monoid[A] extends Semigroup[A] {
    def empty: A
  }

  object Monoid {
    def apply[A](implicit monoid: Monoid[A]): Monoid[A] =
      monoid

    implicit val booleanOrMonoid: Monoid[Boolean] = new Monoid[Boolean] {
      override def empty: Boolean = false

      override def combine(x: Boolean, y: Boolean): Boolean = x || y
    }

    implicit val booleanAndMonoid: Monoid[Boolean] = new Monoid[Boolean] {
      override def empty: Boolean = true

      override def combine(x: Boolean, y: Boolean): Boolean = x && y
    }

    implicit val booleanXorMonoid: Monoid[Boolean] = new Monoid[Boolean] {
      override def empty: Boolean = false

      override def combine(a: Boolean, b: Boolean): Boolean = (a && !b) || (!a && b)
    }

    implicit val booleanXnorMonoid: Monoid[Boolean] = new Monoid[Boolean] {
      override def empty: Boolean = true

      override def combine(a: Boolean, b: Boolean): Boolean = (a || !b) && (!a || b)
    }
  }
}

object AllSetForMonoidsExercise {
  trait Semigroup[A] {
    def combine(x: A, y: A): A
  }

  trait Monoid[A] extends Semigroup[A] {
    def empty: A
  }

  object Monoid {
    def apply[A](implicit monoid: Monoid[A]): Monoid[A] =
      monoid


    implicit def setUnionMonoid[T]: Monoid[Set[T]] = new Monoid[Set[T]] {
      override def empty: Set[T] = Set.empty[T]

      override def combine(x: Set[T], y: Set[T]): Set[T] = x union y
    }

    implicit def setIntersectionSemigroup[T]: Semigroup[Set[T]] = { (x: Set[T], y: Set[T]) => x intersect y }
  }

}

object CatsMonoidSemigroupInstances {

  import cats.Monoid
  import cats.Semigroup
  import cats.instances.string._
  import cats.instances.int._
  import cats.instances.option._
  import cats.syntax.option._

  val helloWorld1: String = Monoid[String].combine("Hello, ", "World!")
  // Hello, World!
  val empty: String = Monoid[String].empty
  // ""

  val semigroupHelloWorld: String = Semigroup[String].combine("Hello, ", "World!")
  // Hello, World!

  val addition: Int = Monoid[Int].combine(30, 3)
  // 33

  val maybeAdd: Option[Int] = Monoid[Option[Int]].combine(30.some, 3.some)
  // Some(33)

}

object CatsMonoidSyntax {
  import cats.Monoid
  import cats.instances.string._
  import cats.instances.int._
  import cats.syntax.semigroup._

  val helloWorld: String = "Hello," |+| " World!" |+| Monoid[String].empty

  val addition: Int = 3 |+| 4 |+| Monoid[Int].empty

}

object AddingAllTheThings {
  import cats.Monoid
  import cats.instances.int._
  import cats.instances.option._
  import cats.syntax.semigroup._

  def add[A: Monoid](items: List[A]): A =
    items.foldLeft(Monoid[A].empty)(_ |+| _)

  val addNumbers: Int = add(List(5, 1, 9))
  val addOptionNumbers: Option[Int] = add(List(Some(4), None, Some(3)))

  case class Order(totalCost: Double, quantity: Double)
  object Order {
    import cats.instances.double._

    implicit val monoidInstance: Monoid[Order] = new Monoid[Order] {
      override def empty: Order = Order(0, 0)

      override def combine(x: Order, y: Order): Order =
        Order(x.totalCost |+| y.totalCost, x.quantity |+| y.quantity)
    }
  }

  val addOrders: Order = add(List(Order(3.3, 65), Order(4.5, 12), Order(9.2, 23)))

}

/*
Side note:
Commutativity - changing the order of operands does not change the result
Associativity - changing the order of the operator does not change the result
 */

/**
 * In Summary:
 *  - A Semigroup represents an addition or combination operation.
 *  - A monoid extends a Semigroup by adding an identity or "zero" element.
 */

