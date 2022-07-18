package RTJVM

object Variance {

  class Vehicle

  case object Bike extends Vehicle

  case object Car extends Vehicle

  class InvariantParking[T](var things: Seq[T]) {
    def park(vehicle: T) = {
      things = vehicle +: things
      things
    }

    def impound(vehicles: Seq[T]) = {
      things = vehicles ++ things
      things
    }


    def checkVehicles(conditions: T => Boolean) = {
      things = things.filter(conditions)
      things
    }
  }


  class CovariantParking[+T](val things: Seq[T]) {
    def park[A >: T](vehicle: A) = {
      val newThings = vehicle +: things
      newThings
    }

    def impound[A >: T](vehicles: Seq[A]) = {
      val newThings = vehicles ++ things
      newThings
    }

    def checkVehicles(conditions: T => Boolean) = {
      val newThings = things.filter(conditions)
      newThings
    }
  }

  class ContravariantParking[-T](things: List[T]) {
    def park(vehicle: T): ContravariantParking[T] = ???

    def impound(vehicles: List[T]): ContravariantParking[T] = ???

    def checkVehicles[S <: T](conditions: S => Boolean): List[S] = ???
  }

}
