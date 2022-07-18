package com.dyptan.implicits

import com.dyptan.{ByteCodec, ByteDecoder}
import com.dyptan.CodecsImpl._

object Codecs {
  implicit class IntByteEncoder(a: Int){
    def encode = IntByteCodec.encode(a)
  }
  implicit class StringByteEncoder(a: String){
      def encode = StringByteCodec.encode(a)
    }

  implicit class ByteDecodeOps[A](in: Array[Byte]){
    def decode(implicit decoder: ByteCodec[A]): Option[A] = {
      decoder.decode(in)
    }
  }


}
