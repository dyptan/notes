package de.riskident

import java.io.{ByteArrayInputStream, InputStream}
import java.nio.charset.StandardCharsets
import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

object OrderProcessor {

  /**
    * @param in An InputStream, which contains the following input:
    *           A line containing a single number: The number of guests N,
    *           Followed by N lines containing two numbers Ti and Li separated by space.
    *           There may be a trailing newline.
    *           Ti ist the ordering time for Ni, Li is the time it takes to bake Ni's pizza.
    *           0 <= N  <= 100000
    *           0 <= Ti <= 1000000000
    *           1 <= Li <= 1000000000
    * @return   A Right containing the integer part of the average waiting time if the input is valid.
    *           A Left containing a syntax error description otherwise.
    */
  def process(in: InputStream): Either[String, Long] = {
    val pizzaOrders = Option(new String(in.readAllBytes()))

    var numOrders = 0
    var orders: Seq[Tuple2[Int,Int]] = Seq.empty

    pizzaOrders.map(string => string.split("\n")).foreach{
      array => numOrders = array(0).toInt
          for (order <- array) {
            parseOrder(order) match {
            case Some(order) => orders = orders :+ order
            case None => orders
            }
          }
          orders
    }

    if (numOrders <= 0 || numOrders >= 100000) return Left("number of orders must be between 0 and 10^5")

    orders.foreach{
      case a if a._1 < 0 || a._1 > 10e9 => return Left(s"order ${a} does not conform to restrictions")
      case a if a._2 < 1 || a._2 > 10e9 => return Left(s"order ${a} does not conform to restrictions")
      case _ => ()
      }

    val smallestDurationTime = new Ordering[Tuple2[Int,Int]] {
      override def compare(x: (Int, Int), y: (Int, Int)): Int = x._2 - y._2
    }


    def calculateOrdersTotalTimes(orders: Seq[(Int,Int)]): Seq[Int] = {
      @tailrec
      def recursiveHelper(remainingOrders: Seq[(Int,Int)],
                          orderReadyTime: Int,
                          orderInQueueWaitingTime: Int,
                          orderTotalTime: Int,
                          currentOrder: Tuple2[Int,Int],
                          processedOrdersTotalTimes: Seq[Int]): Seq[Int] = {

        if (remainingOrders == Seq.empty) return processedOrdersTotalTimes

        val newCurrentOrder = remainingOrders.filterNot(a => a == currentOrder).min(smallestDurationTime)
        val newRemainingOrders = remainingOrders.filterNot(a => a == newCurrentOrder)
        val newOrderInQueueWaitingTime = orderReadyTime - newCurrentOrder._1
        val newOrderReadyTime = orderReadyTime + newCurrentOrder._2
        val newOrderTotalTime = newOrderInQueueWaitingTime + newCurrentOrder._2
        val newProcessedOrdersTotalTimes = processedOrdersTotalTimes :+ newOrderTotalTime

        recursiveHelper(newRemainingOrders, newOrderReadyTime, newOrderInQueueWaitingTime, newOrderTotalTime, newCurrentOrder, newProcessedOrdersTotalTimes)
      }

      recursiveHelper(orders,0,0,0,(0,0),Seq.empty)
    }


    val averageWaitingTime = calculateOrdersTotalTimes(orders).sum/numOrders
    Right(averageWaitingTime)

  }

  def parseOrder(unsafeOrder: String): Option[Tuple2[Int, Int]] = {
    val pair: Array[String] = unsafeOrder.split(" ")
    val attempt = Try {
      (pair(0).toInt, pair(1).toInt)
    }
    attempt match {
      case Success(value) => Some(value)
      case Failure(_) => None
    }
  }

  def main(args: Array[String]) = {
    val input =
      """3
        |0 3
        |1 9
        |2 5
        |""".stripMargin
    val inStream: InputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))
    print(process(inStream))

  }

}
