package RTJVM

object Variance {

  class Vehicle

  case object Bike extends Vehicle

  case object Car extends Vehicle

  class InvariantParking[T](var things: List[T]) {
    def park(vehicle: T): List[T] = {
      things = vehicle +: things
      things
    }

    def impound(vehicles: List[T]): List[T] = {
      things = vehicles ::: things
      things
    }

    def checkVehicles(conditions: T => Boolean): List[T] = {
      things = things.filter(conditions)
      things
    }
  }


  class CovariantParking[+T](val things: List[T]) {
    def park[A >: T](vehicle: A) = {
      val newThings = vehicle +: things
      newThings
    }

    def impound[A >: T](vehicles: List[A]): List[A] = {
      val newThings = vehicles ::: things
      newThings
    }

    def checkVehicles(conditions: T => Boolean): List[T] = {
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
