package everydaycoding

object ProductOfArrayElems {


  def productImperative(input: Array[Int]) = {
    val result = new Array[Int](input.length)
    for (i <- 0 to input.length-1) {
      var intermediate = 1
      for (j <- 0 to input.length-1) {
        if (i!=j) {
          intermediate = intermediate * input(j)
        }
      }
      result(i) = intermediate
    }
    result.toList
  }

def procuctRecursive(input: Array[Int])={
  def helper(input: Array[Int], acc: Int, index: Int = input.length): Unit ={
    if (index == 0) return 1
    helper(input, acc * input(index), index-1)
  }
}



def main(args: Array[String])= {

  val array = Array(1,3,5,10)
  println("hello")
  print(productImperative(array))
  }
}

