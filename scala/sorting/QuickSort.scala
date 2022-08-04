package sorting



object QuickSort {

  def quickSort(array: Array[Int], pointerLeft: Int, pointerRight: Int): Unit = {
    if (pointerLeft < pointerRight)  {
      val index = partition(array, pointerLeft, pointerRight)
        quickSort(array, pointerLeft, index - 1)

        quickSort(array, index, pointerRight)

    }
  }

  def quickSort(array: Array[Int]): Unit = {
    quickSort(array, 0, array.length - 1)
  }

  def swap(array: Array[Int], left: Int, right: Int) = {
    val temp = array(left)
    array(left) = array(right)
    array(right) = temp
  }

  def partition(array: Array[Int], initialLeft: Int, initialRight: Int): Int = {
//    val pivotIndex = initialLeft+(new java.util.Random()).nextInt(initialRight-initialLeft)
    val pivotIndex = initialRight
    val pivot = array(pivotIndex)
    var left = initialLeft
    var right = initialRight

    while (left < right) {
      //if value less then pivot OK move on
      while  ( left < right && array(left) <= pivot ){
        left += 1
      }
      //if value greater then pivot OK move on
      while  ( left < right && array(right) >= pivot ){
        right -= 1
      }
      //if all values were less or greater than pivot = the split already sorted -> exit

        swap(array, left, right)

    }

    // move pivot in between sorted splits
    swap(array, left, pivotIndex)

    left
  }



  def main(args: Array[String]) = {
    val array = Array(4,3, 0, 1)
    quickSort(array)
    println(array.toList)
  }
}
