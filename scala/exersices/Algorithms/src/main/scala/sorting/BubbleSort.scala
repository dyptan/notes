package sorting

object BubbleSort {

  object BubbleSort {
    def sort(array: Array[Int]): Unit = {
      for (i <- 0 until array.length) {
        for (j <- 0 until array.length - 1 - i) {
          if (array(j) > array(j + 1)) {
            val temp = array(j)
            array(j) = array(j + 1)
            array(j + 1) = temp
          }
        }
      }
    }

    def main(args: Array[String]): Unit = {
      val myarray = Array(1, 8, 5, 3, 10)
      sort(myarray)
      for (element <- myarray) {
        System.out.println(element)
      }
    }
  }

}
