val array = Array(1,3,5,10)
println("hello")

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


//print(productOfValues(array))
