package com.dyptan

import java.nio.ByteBuffer
import scala.util.Try

object CodecsImpl {

  implicit object StringByteCodec extends ByteCodec[String] {
    override def decode(array: Array[Byte]): Option[String] =
      Option(new String(array))

    override def encode(obj: String): Array[Byte] =
      obj.getBytes
  }

  implicit object IntByteCodec extends ByteCodec[Int] {
    override def encode(obj: Int): Array[Byte] = {
      val bb = ByteBuffer.allocate(4)
      bb.putInt(obj)
      bb.array()
    }

    override def decode(array: Array[Byte]): Option[Int] = {
      array match {
        case array if array.length == 4 => {
          val bb = ByteBuffer.allocate(4)
          bb.put(array)
          bb.flip()
          Some(bb.getInt)
        }
        case _ => None
      }
    }
  }

  case class Switch(isOn: Boolean)
  object Switch {
    val decoder: ByteDecoder[Switch] = ByteDecoder.instance{
      s => SwitchByteCodec.decode(s)
    }

    implicit object SwitchByteCodec extends ByteCodec[Switch] {
      override def encode(obj: Switch): Array[Byte] = {
        Array(if (obj.isOn) '1'.toByte else '0'.toByte)
      }

      override def decode(array: Array[Byte]): Option[Switch] = array match {
        case array if array(0) == '0'.toByte => Some(Switch(false))
        case array if array(0) == '1'.toByte => Some(Switch(true))
        case _ => None
      }
    }
  }
}