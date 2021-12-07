package RTJVM.test

import RTJVM._
import org.scalatest.funspec.AnyFunSpec

class MySetSuit extends AnyFunSpec{

  describe("covariant Set"){
    it ("creates EmptySet"){
      assert(MySet()==EmptySet)
    }
    it("creates non emply set"){
      assert(MySet(1).isInstanceOf[NonEmptySet[Int]])
    }
    it("adds values to set"){
      val setWithTwoValues = MySet(1)+2
      assert(setWithTwoValues.tail.head == 1 &&
        setWithTwoValues.head == 2)
    }
    it("adds values from another LinkedSet"){
      val concatenatedSet = MySet(1) + 2 ++ MySet(3)
      assert(concatenatedSet.contains(3) && concatenatedSet.contains(1))
    }
    it("transforms values with Map"){
      val transformedSet = (MySet(1) + 2).map(_*2)
      assert(transformedSet.contains(4))
    }
    it("transforms and flattens folded Sets with FlatMap"){
      val flattenedSet = (MySet(1) + 2).flatMap(x => MySet(x*2))
      assert(!flattenedSet.contains(1) && flattenedSet.contains(4))
    }
    it("returns only filtered values"){
      val filteredSet = (MySet(1) + 2).filter(_%2==0)
      assert(!filteredSet.contains(1) && filteredSet.contains(2))
    }
    it("produces only side-effect with no mutations to values"){
      val setWithTwoValues = MySet(1)+2
      setWithTwoValues.foreach(println)
      assert(setWithTwoValues.contains(1) && setWithTwoValues.contains(2))
    }
  }

}
