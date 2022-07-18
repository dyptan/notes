package com.dyptan

import java.io.{FileInputStream, FileOutputStream}
import scala.util.Using

object Package {

  trait Channel {
    def write[A](obj: A)(implicit encoder: ByteCodec[A]): Unit
    def read[A](file: String)(implicit decoder: ByteCodec[A]): Option[A]
  }

  object FileChannel extends Channel {
    override def write[A](obj: A)(implicit codec: ByteCodec[A]): Unit =
      Using(new FileOutputStream("myfile.txt")) { os =>
        os.write(codec.encode(obj))
        os.flush()
      }

    override def read[A](file: String)(implicit codec: ByteCodec[A]): Option[A] = {
      val data = new Array[Byte](100)
      Using(new FileInputStream(file)) {
        is =>
          is.read(data)
          is.close()
      }
      codec.decode(data)
    }
  }


  def main(args: Array[String]): Unit = {

    import com.dyptan.implicits.Codecs.ByteDecodeOps
    import com.dyptan.CodecsImpl.IntByteCodec

    val input = Array[Byte](0, 0, 5, 5)

    print(input.decode)
  }


}
