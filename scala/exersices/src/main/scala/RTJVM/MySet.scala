package RTJVM

trait MySet[A] extends (A => Boolean) {

  def contains(elem: A): Boolean

  def +(elem: A): MySet[A]

  def ++(anotherSet: MySet[A]): MySet[A]

  def map[B](f: A => B): MySet[B]

  def flatMap[B](f: A => MySet[B]): MySet[B]

  def filter(predicate: A => Boolean)

  def foreach(f: A => Unit): Unit
}

object MySet {
  def apply[A](input: A): MySet[A] = {
    MySetImpl(input)
  }
}


case class MySetImpl[A](private val value: A) extends MySet[A]{
  private var values = value :: List.empty
  private [RTJVM] def getValues = values

  override def contains(elem: A): Boolean = values.contains(elem)

  override def +(elem: A): MySet[A] = {
    if (contains(elem)) return this
    values = elem :: values
    this
  }

  override def ++(anotherSet: MySet[A]): MySet[A] = {
    anotherSet.foreach(+)
    this
  }

  override def map[B](f: A => B): MySet[B] = MySet(f(value))

  override def flatMap[B](f: A => MySet[B]): MySet[B] = f(value)

  override def filter(predicate: A => Boolean): Unit = values = values.filter(predicate)

  override def foreach(f: A => Unit): Unit = values.foreach(f)

  override def apply(v1: A): Boolean = contains(v1)
}



