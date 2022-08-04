object Main {
  def main(args: Array[String]): Unit = {

      def process(in: String) = {
//        val chars = in.toList
        val indexedSeq = in.map{
          case x if (x.isUpper) => s"_${x.toLower}"
          case x => String.valueOf(x)
        }
        indexedSeq.mkString
      }

    print(process("camelCaseOfSomething"))
  }
}