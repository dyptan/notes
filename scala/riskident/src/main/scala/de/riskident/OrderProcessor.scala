package de.riskident

import java.io.{ByteArrayInputStream, InputStream}
import java.nio.charset.StandardCharsets
import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

case class Order(processingTime: Int, arrivalTime: Int)

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
  def process(in: InputStream): Either[String, Any] = {
    val pizzaOrders = Try(new String(in.readAllBytes()))

    val rawInput: Try[List[String]] = pizzaOrders.map(string => string.split("\n").toList)
    val numOrders: Try[Int] = rawInput.map(_(0)).map(_.toInt)
    val validatedNumOrders = numOrders.map(x => if (x <= 0 || x >= 100000) Left("number of orders must be between 0 and 10^5") else Right(x))

    def parseOrder(unsafeOrder: String): Either[String,Order] = {
      val pair: Array[String] = unsafeOrder.split(" ")
      val attempt = Try {
        Order(pair(0).toInt, pair(1).toInt)
      }
      attempt match {
        case Success(value) => Right(value)
        case Failure(e) => Left(s"pair ${pair.mkString} cannot be converted to Order because of ${e}")
      }
    }

    val parsedOrders: Seq[Either[String, Order]] = rawInput.get.drop(1).map(parseOrder)

    val checkedOrders = for {
      orderEither <- parsedOrders
    } yield orderEither match {
      case Left(a) => Left(a)
      case Right(a) if a.processingTime < 0 || a.processingTime > 10e9 => Left(s"order ${a} does not conform to restrictions")
      case Right(a) if a.arrivalTime < 1 || a.arrivalTime > 10e9 => Left(s"order ${a} does not conform to restrictions")
      case Right(a) => Right(a)
    }

//    val validOrders = checkedOrders.flatMap(_.toSeq)//check if invalid
//    checkedOrders.foreach(println)//.flatMap(_.toSeq)//check if invalid
    //validOrders.foreach(println)

    val smallestDurationTime = new Ordering[Order] {
      override def compare(x: Order, y: Order): Int = {
        x.arrivalTime - y.arrivalTime
      }
    }



    def calculateOrdersTotalTimes(orders: Seq[Order]): Seq[Long] = {
      @tailrec
      def recursiveHelper(remainingOrders: Seq[Order],
                          orderReadyTime: Long,
                          orderInQueueWaitingTime: Long,
                          orderTotalTime: Long,
                          currentOrder: Order,
                          processedOrdersTotalTimes: Seq[Long]): Seq[Long] = {

        if (remainingOrders == Seq.empty) return processedOrdersTotalTimes

        val newCurrentOrder = remainingOrders.filterNot(a => a == currentOrder).min(smallestDurationTime)
        val newRemainingOrders = remainingOrders.filterNot(a => a == newCurrentOrder)
        val newOrderInQueueWaitingTime =  orderReadyTime - newCurrentOrder.processingTime
        val newOrderReadyTime = orderReadyTime + newCurrentOrder.arrivalTime
        val newOrderTotalTime = newOrderInQueueWaitingTime + newCurrentOrder.arrivalTime
        val newProcessedOrdersTotalTimes = processedOrdersTotalTimes :+ newOrderTotalTime

        recursiveHelper(newRemainingOrders, newOrderReadyTime, newOrderInQueueWaitingTime, newOrderTotalTime, newCurrentOrder, newProcessedOrdersTotalTimes)
      }
      recursiveHelper(orders,0,0,0,Order(0,0),Seq.empty)
    }

     if (checkedOrders.filter(_.isLeft).isEmpty) {
      val allWaitingTimes = calculateOrdersTotalTimes(checkedOrders.flatMap(_.toSeq)).sum
      validatedNumOrders.get match {
        case Right(numberOfOrders) => Right(allWaitingTimes / numberOfOrders)
        case Left(b) => Left(b)
      }
    } else {
      checkedOrders.find(_.isLeft).get
      }

  }



  def main(args: Array[String]) = {
    val input =
      """3
        |0 3
        |s 9
        |2 6
        |
        |""".stripMargin
    val inStream: InputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))
    print(process(inStream))

  }

}
