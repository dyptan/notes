package com.dyptan

trait ByteCodec[A] extends ByteEncoder[A] with ByteDecoder[A] {
  def isoporshism(a: A)(implicit codec: ByteCodec[A]): Boolean = {
    codec.decode(codec.encode(a)) == Some(a)
  }
}

object ByteCodec {
  def apply[A](implicit ev: ByteCodec[A]) = ev
}

trait ByteDecoder[A]{
  def decode(array: Array[Byte]): Option[A]
}

object ByteDecoder{
  def instance[A](f: Array[Byte] => Option[A]): ByteDecoder[A] = new ByteDecoder[A] {
    def decode(array: Array[Byte]): Option[A] = {
      f(array)
    }
  }
}

trait ByteEncoder[A] {
  def encode(obj: A): Array[Byte]
}

