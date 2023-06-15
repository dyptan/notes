import java.io.FileReader
import java.nio.file.{Files, Paths}
import scala.io.Source

object Exercise {

    def runningSum(nums: List[Int]): List[Int] = {
      def helper(remainder: List[Int], acc: List[Int]): List[Int] = {
        if (remainder.isEmpty) return acc
        remainder match {
          case _ :: t => helper(t, acc :+ remainder.sum)
        }
      }
      helper(nums.reverse, Nil)
    }

    def maximumWealth(accounts: Array[Array[Int]]): Int = {
      accounts.toVector.map(customer => customer.toVector.sum).max
    }

    def fizzBuzz(n: Int): List[String] = {
      val digits = Range.inclusive(1,n).toList
      digits.map {
        case h if h % 3 == 0 && h % 5 == 0 => "FizzBuzz"
        case h if h % 3 == 0 => "Fizz"
        case h if h % 5 == 0 => "Buzz"
        case h => h.toString
      }
    }

      def canConstruct(ransomNote: String, magazine: String): Boolean = {
        def groupChars(input: String) = {
          input.foldLeft(Map.empty[Char, Int])(
            (map: Map[Char, Int], next: Char) => map.updated(next, map.get(next).getOrElse(0) + 1))
        }
        val groupedLettersFromNote = groupChars(ransomNote)
        val groupedLettersFromMagazine = groupChars(magazine)
        val testedPairs = groupedLettersFromNote.map{
          case a if groupedLettersFromMagazine.getOrElse(a._1, 0) >= a._2 => true
          case _ => false
        }
        if (testedPairs.isEmpty) return false
        testedPairs.forall(_.equals(true))
      }

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

