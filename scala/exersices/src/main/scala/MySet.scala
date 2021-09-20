trait MySet[A] extends (A => Boolean) {

  def contains(elem: A): Boolean
  def +(elem:A): MySet[A]
  def ++(anotherSet: MySet[A]): MySet[A]
  def map[B](f: A => B): MySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B]
  def filter (predicate: A=> Boolean)
  def foreach(f: A=> Unit): Unit
}

class MySetImpl[A](values: List[A]) extends MySet[A]{
  override def contains(elem: A): Boolean = ???

  override def +(elem: A): MySet[A] = {
    if (values.contains(elem)) this
    else MySetImpl[A](elem +: values)
  }

  override def ++(anotherSet: MySet[A]): MySet[A] = anotherSet.flatMap(+)

//  override def map[B](f: A => B): MySet[B] = flatMap(f)

  override def flatMap[B](f: A => MySet[B]): MySet[B] = values.map(f).foldRight(MySetImpl(List[Nothing]))(++(_))

  override def filter(predicate: A => Boolean): Unit = ???

  override def foreach(f: A => Unit): Unit = ???

  override def apply(v1: A): Boolean = contains(v1)
}


object MySetImpl {
  def apply[A](input: List[A]): MySet[A] = {
        new MySetImpl[A](input)
  }
}
