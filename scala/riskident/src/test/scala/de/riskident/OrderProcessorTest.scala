package de.riskident

import java.io.{ByteArrayInputStream, InputStream}
import java.nio.charset.StandardCharsets

import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.{FunSuite, MustMatchers}

class OrderProcessorTest extends FunSuite with MustMatchers with TypeCheckedTripleEquals {

  test("provided test data") {
    val input =
      """3
        |0 3
        |1 9
        |2 6
        |""".stripMargin
    val inStream: InputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))
    OrderProcessor.process(inStream) must === (Right(9L))
  }
}
