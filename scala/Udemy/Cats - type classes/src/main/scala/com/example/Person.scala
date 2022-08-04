package com.example

case class Person(name: String, id: Int)

object Instances {
  // TODO #9: Define an Eq instance for Person comparing them by name
  //          Extra points: receive an implicit instance for String and use it

  implicit object PersonEqNames extends Eq[Person] {
    override def eq(a: Person, b: Person) = {
      Eq[String](a.name, b.name)
    }
  }

  // TODO #10: Define an Eq instance for Person comparing them by id
  //           Extra points: receive an implicit instance for Int and use it

  implicit object PersonEqId extends Eq[Person] {
    override def eq(a: Person, b: Person) = {
      Eq[Int](a.id, b.id)
    }
  }


}