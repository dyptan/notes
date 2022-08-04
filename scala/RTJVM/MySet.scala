package RTJVM

trait MySet[+A]{
  def contains[B>:A](elem: B): Boolean
  def +[B>:A](elem: B): MySet[B]
  def ++[B>:A](anotherSet: MySet[B]): MySet[A]
  def map[B](f: A => B): MySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B]
  def filter(predicate: A => Boolean): MySet[A]
  def foreach(f: A => Unit): Unit
}

object MySet {
  def apply[A](input: A): MyLinkedSet[A] = {
    MyLinkedSet(input)
  }
  def apply[A](): MyLinkedSet[A] = {
    EmptySet
  }
}




