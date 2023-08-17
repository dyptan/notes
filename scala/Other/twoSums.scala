object Test {
    def twoSums(numbers: Seq[Int], target: Int): (Int, Int) = {
        var left = 0
        var right = 1
        while left < right do
            while right < numbers.size do
                if target - numbers(left) == numbers(right) then
                    return (left, right)
                else
                    right += 1
            left += 1
            right = left+1
        return (-1, -1)
    }

    def recSum(numbers: Seq[Int], target: Int): (Int, Int) = {
        @scala.annotation.tailrec
        def helper(left: Int, right: Int): (Int, Int) =  
            left match
                case _ if left == numbers.size -1 => (-1, -1)
                case _ if numbers(right) == target - numbers(left) => (left, right)
                case _ if right < numbers.size - 1 => helper(left, right+1)
                case _ => helper(left+1, left+2)
        helper(0,1)
    }

    def main(args: Array[String]) = {
        print(recSum(Array(1,2,3,4,5),3))
    }


}
