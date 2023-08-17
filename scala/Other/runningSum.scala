 def runningSum(nums: List[Int]): List[Int] = {
      def helper(remainder: List[Int], acc: List[Int]): List[Int] = {
        if (remainder.isEmpty) return acc
        remainder match {
          case _ :: t => helper(t, acc :+ remainder.sum)
        }
      }
      helper(nums.reverse, Nil)
 }
