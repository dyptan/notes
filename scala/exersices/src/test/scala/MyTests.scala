
import Variance._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.funsuite.AnyFunSuite

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
}
