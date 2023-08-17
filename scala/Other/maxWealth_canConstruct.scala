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
