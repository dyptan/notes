package com.example

trait Eq[A] {
  // TODO #1: Define an 'eq' method that takes two A values as parameters, and returns a Boolean
  def eq(a: A, b: A): Boolean
}

object Eq {

  // TODO #2: Define the method 'apply' so we can summon instances from implicit scope
  def apply[A](a: A, b: A)(implicit instance: Eq[A]) = {
    instance.eq(a,b)
  }

  // TODO #3: Define the method 'instance' so we can build instances of the Eq typeclass more easily.
  //          It should take as the only parameter a function of type (A, A) => Boolean
  def instance[A](f: (A, A) => Boolean) = new Eq[A] {
    override def eq(a: A, b: A) = {
      f(a,b)
    }
  }

  // TODO #4: Define an Eq instance for String

  implicit object StringEq extends Eq[String]{
    override def eq(a: String, b: String) = {
      a == b
    }
  }

  // TODO #5: Define an Eq instance for Int

  implicit object IntEq extends Eq[Int]{
    override def eq(a: Int, b: Int) = {
      a == b
    }
  }

  // TODO #6: Define an Eq instance for Person. Two persons are equal if both their names and ids are equal.
  //          Extra points: receive implicit instances for String and Int and use them

  implicit object PersonEq extends Eq[Person] {
    override def eq(a: Person, b: Person) = {
      Eq[String](a.name,b.name) && Eq[Int](a.id, b.id)
    }
  }

  // TODO #7: Provide a way to automatically derive instances for Eq[Option[A]] given that we have an implicit
  //          instance for Eq[A]

  implicit def optionEq[A](implicit equaliser: Eq[A]): Eq[Option[A]] = new Eq[Option[A]] {
    def eq(a: Option[A], b: Option[A]): Boolean = {
      val optionalBool = for {
        valA <- a
        valB <- b
      } yield equaliser.eq(valA, valB)

      optionalBool.getOrElse(false)
    }


    // TODO #8: Define a class 'EqOps' with a method 'eqTo' that enables the following syntax:
    //          "hello".eqTo("world")
  }
    object Syntax {
      implicit class EqOps(current: String) {
        def eqTo(another: String): Boolean = {
          StringEq.eq(current, another)
        }
      }
    }


  def main(args:Array[String]): Unit= {
    import Eq.Syntax.EqOps
//    print("hello".eqTo("hello"))

    val out = optionEq[Person].eq(Some(Person("Ivan", 32)), None)

    import Instances.PersonEqId
    val out2 = Eq[Person](Person("Ivan", 32), Person("Ivango", 32))(PersonEqId)

    print(out2)
    }
}