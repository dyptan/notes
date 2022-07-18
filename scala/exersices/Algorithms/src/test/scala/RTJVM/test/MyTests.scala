package RTJVM.test

import RTJVM.{MySet, NonEmptySet}
import RTJVM._
import RTJVM.Variance._
import org.scalatest.funspec.AnyFunSpec

class MyTests extends AnyFunSpec {

  val vehicles: List[Vehicle] = List(Car, Bike, Car)
  val vehiclesWithOneMoreBike: List[Vehicle] = Bike +: vehicles
  val threeBikes =  List(Bike, Bike, Bike)
  val vehiclesWithOneMoreBikePlusTreeBikes = threeBikes ::: vehiclesWithOneMoreBike

  val onlyBikes: Vehicle=> Boolean = {
    case Bike => true
    case _ => false
  }
  val onlyBikesVehicles = vehiclesWithOneMoreBikePlusTreeBikes.filter(onlyBikes)


  describe("invariant Parking"){
    val myVehicles = new InvariantParking[Vehicle](vehicles)
    it("create parking") {
      assert(myVehicles.things===vehicles)
    }
    it("add Bike") {
      assert(myVehicles.park(Bike)===vehiclesWithOneMoreBike)
    }
    it("impound tree bikes") {
      assert(myVehicles.impound(List(Bike, Bike, Bike))===vehiclesWithOneMoreBikePlusTreeBikes)
    }
    it("get all bikes") {
      assert(myVehicles.checkVehicles(onlyBikes)===onlyBikesVehicles)
    }
  }

  describe("covariant Parking"){
    val myVehicles = new CovariantParking[Vehicle](vehicles)
    it("create parking") {
      assert(myVehicles.things===(vehicles))
    }
    it("add Bike") {
      assert(myVehicles.park(Bike)===(vehiclesWithOneMoreBike))
    }
    it("impound tree bikes") {
      assert(myVehicles.impound(List(Bike, Bike, Bike))===(threeBikes ::: vehicles))
    }
    it("get all bikes") {
      assert(myVehicles.checkVehicles(onlyBikes)===(myVehicles.things.filter(onlyBikes)))
    }
  }

  describe("MySet"){
    var mySet = NonEmptySet(0)
    it("new Myset should CONTAIN value 0"){
      assert(mySet.contains(0)===true)
    }
    it("new Myset should NOT CONTAIN value 1") {
      assert(mySet.contains(1) === false)
    }

    it("Myset should add value 1"){
      assert((mySet + 1).contains(1)===true)
    }

    it("new Myset should ADD values from another MySet"){
      assert(
        (mySet ++ (MySet(3) + 4)).contains(3)===true &&
        (mySet ++ (MySet(3) + 4)).contains(4)===true
          )
    }

    it("adding already contained value should be ignored"){
      mySet + 1
      assert((mySet.getValues.count(x=>x==1)==1)===true)
    }

  }
}
