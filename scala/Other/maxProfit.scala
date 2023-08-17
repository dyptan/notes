import java.io.FileReader
import java.nio.file.{Files, Paths}
import scala.io.Source

object Exercise {

  def maxProfit(prices: Array[Int]): Int = {
    var maxSoFar = 0;
    var left = 0
    var right = 1
    while (left < prices.length - 1) {
      while (right < prices.length) {
        val delta = prices(right) - prices(left)
        if (delta > maxSoFar) {
          maxSoFar = delta
        }
          right += 1
      }
      left += 1
      right = left + 1
    }
    maxSoFar
  }

  def maxProfitRec(prices: Array[Int]): Int = {
    def helper(remainder: List[Int], maxSoFar: Int): Int = {
      if (remainder.isEmpty) return maxSoFar
      remainder match {
        case h :: h2 :: t if h2 - h > maxSoFar => helper(h::t, h2 - h)
        case _ :: t => helper(t, maxSoFar)
      }
    }

    helper(prices.toList, 0)
  }

  def main(args: Array[String]): Unit ={
    val input = Source.fromFile("numbers.txt").getLines()
      .flatMap(_.split(','))
      .map(i => Integer.parseInt(i)).toArray

    val myinput = Array(4,12,3,10)
    println(maxProfit(myinput))
//    println(canConstruct("ihhgg", "ch"))
    }

}

