package com.dvdkly.scalawithcats.ch01.typeclasses

object JsonTypeclass {

  // Typeclasses have 3 components
  // 1. The typeclass itself
  // 2. Instances for particular types
  // 3. Interface methods

  // Define a very simple JSON AST
  sealed trait Json
  final case class JsObject(get: Map[String, Json]) extends Json
  final case class JsString(get: String) extends Json
  final case class JsNumber(get: Double) extends Json
  case object JsNull extends Json

  // 1. A JsonWriter Typeclass
  // The "serialize to JSON" behaviour is encoded in this trait
  trait JsonWriter[A] {
    def write(value: A): Json
  }

  // 2. instances
  // An instance is defined by creating a concrete implementation of the type class
  // and tagging it with the scala `implicit` keyword.
  final case class Person(name: String, email: String)
  object JsonWriterInstances {
    implicit val stringWriter: JsonWriter[String] =
      new JsonWriter[String] {
        def write(value: String): Json =
          JsString(value)
      }
    implicit val personWriter: JsonWriter[Person] =
      new JsonWriter[Person] {
        def write(value: Person): Json =
          JsObject(
            Map(
              "name" -> JsString(value.name),
              "email" -> JsString(value.email)
            )
          )
      }
    // etc...
  }

  // 3. Interface
  // Interfaces are generic methods that accept instances of the type class as implicit parameters.
  object Json {
    def toJson[A](value: A)(implicit w: JsonWriter[A]): Json =
      w.write(value)
  }

  // Interface Syntax / Extension Methods
  // Alternatively, we can define extension methods to extend existing types with interface methods.
  // Cats refers to this as syntax
  // Sometimes in the past, extension methods were referred to as "type enrichment" or "pimping".

  object JsonSyntax {
    implicit class JsonWriterOps[A](value: A) {
      def toJson(implicit w: JsonWriter[A]): Json = w.write(value)
    }
  }

  // An example of some code using the JsonWriter typeclass
  object ExampleUsingJsonWriterTypeclass {
    // First import any type class instances required
    import JsonWriterInstances._
    // Then call the relevant method as required
    // The compiler searches for typeclass instances available and automatically passes the implicit parameter
    // to the method.
    Json.toJson(Person("David", "example@example.com"))
  }

  // An example of some code using the JsonWriter typeclass with syntax
  object ExampleUsingJsonWriterTypeclassWithSyntax {
    import JsonWriterInstances._
    import JsonSyntax._

    Person("David", "example@example.com").toJson
    "stuff".toJson
  }

  object ImplicitlyMethod {
    // The scala standard lib provides a generic type class interface called implicitly
    def implicitly[A](implicit value: A): A = value
    // It allows us to summon any value from implicit scope.
    import JsonWriterInstances._
    val x: JsonWriter[String] = implicitly[JsonWriter[String]]
    // implicitly can be a handy tool for debugging purposes when getting compiler errors about missing instances.
  }

  /*
  On Defining Typeclass Instances
  Instances can be packaged
  1. In an object such as JsonWriterInstances
  2. Placed inside a trait
  3. Inside the companion object of the typeclass
  4. Inside the companion object of the parameter type
   */

  // We may want to define implicit methods to construct instances from other type class instances.
  // eg JsonWriter for options.
  // Take the Person type for example, we don't want to define an instance for Person and one for Option[Person]
  // Lets just define a generic instance for Option[A] instead.

  object JsonWriterOptionInstance {
    implicit def optionWriter[A](implicit
        writer: JsonWriter[A]
    ): JsonWriter[Option[A]] = {
      case Some(aValue) => writer.write(aValue)
      case None         => JsNull
    }
  }

  object ExampleUsingOptionJsonWriter {
    import JsonWriterInstances._
    import JsonWriterOptionInstance._
    import JsonSyntax._

    Option("str").toJson
  }

  /*
  A note on implicit conversions
  When creating a typeclass instance constructor, be sure to mark the parameters to the method as implicit parameters.

  Passing non-implicit parameters to implicit methods is part of a Scala pattern called implicit conversion. This is
  generally frowned upon in modern scala code.
   */

}
