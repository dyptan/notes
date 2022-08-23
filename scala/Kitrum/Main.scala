object Main {
  def main(args: Array[String]): Unit = {

      def camelToSnakeCase(in: String) = {
        val indexedSeq = in.map{
          case x if (x.isUpper) => s"_${x.toLower}"
          case x => String.valueOf(x)
        }
        indexedSeq.mkString
      }

    print(camelToSnakeCase("camelCaseOfSomething"))
  }
}
