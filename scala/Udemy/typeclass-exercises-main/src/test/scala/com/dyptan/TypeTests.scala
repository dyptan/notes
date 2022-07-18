package com.dyptan

import com.dyptan.CodecsImpl.Switch._
import org.scalacheck.Arbitrary
import org.scalacheck.Prop.forAll
import org.typelevel.discipline.Laws
import com.dyptan.CodecsImpl._

import org.scalatest.prop.Configuration

object TypeTests {
  trait ByteCodecLaws[A]{
    def codec: ByteCodec[A]
    def isomorphism(a:A): Boolean = {
      codec.decode(codec.encode(a)) == Some(a)
    }
  }

  trait ByteCodecTests[A] extends Laws {
    def laws: ByteCodecLaws[A]
    def byteCodec(implicit arb: Arbitrary[A]): RuleSet = new DefaultRuleSet(
      name = "byteCodec",
      parent = None,
      "isomorphism" -> forAll(laws.isomorphism _)
    )
  }

  object StringByteCodecLaws extends ByteCodecLaws[String] {
    override def codec: ByteCodec[String] = StringByteCodec
  }

  object StringByteCodecTest extends ByteCodecTests[String]{
    override def laws: ByteCodecLaws[String] = StringByteCodecLaws
  }

  object SwitchByteCodecLaws extends ByteCodecLaws[Switch] {
    override def codec: ByteCodec[Switch] = SwitchByteCodec
  }

  object SwitchByteCodecTest extends ByteCodecTests[Switch]{
    override def laws: ByteCodecLaws[Switch] = SwitchByteCodecLaws
  }
  object IntByteCodecLaws extends ByteCodecLaws[Int] {
      override def codec: ByteCodec[Int] = IntByteCodec
    }

    object IntByteCodecTest extends ByteCodecTests[Int]{
      override def laws: ByteCodecLaws[Int] = IntByteCodecLaws
    }

}

import org.scalatest.funsuite.AnyFunSuite
import org.typelevel.discipline.scalatest.FunSuiteDiscipline
import com.dyptan.TypeTests._

class ByteCodecSpec extends AnyFunSuite with Configuration with FunSuiteDiscipline {
  checkAll("byteCodec[int]", IntByteCodecTest.byteCodec)
  checkAll("byteCodec[String]", StringByteCodecTest.byteCodec)
}


