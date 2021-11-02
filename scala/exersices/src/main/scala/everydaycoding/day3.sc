val array = Array(2,4,-2,-5, 1)


//sortedArray.find(_>0).map(_+1)

val sortedArray = array.sorted.toList

def findLowestMissingPositive(sortedArray: List[Int]):Int =
  sortedArray match {
    case head :: Nil if head >= 0 => 1
    case head :: tail if head >= 0 && tail.head > 1 && tail.head-head > 1 => 1
    case head :: tail if head > 0 && (tail.head-head) > 1 => head + 1
    case head :: tail if head > 0 && (tail.head-head) <= 1 => findLowestMissingPositive(tail)
    case head :: tail => findLowestMissingPositive(tail)
  }


findLowestMissingPositive(sortedArray)