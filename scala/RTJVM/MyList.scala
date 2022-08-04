package RTJVM

import java.util.NoSuchElementException


trait List[+A]{
	def head: A
	def tail: List[A]
	def map[B](f: A => B): List[B]
	def flatMap[B](f: A => List[B]): List[B]
	def ::[S>:A](elem: S): List[S] = new Cons[S](elem, this)
	def :::[S>:A](input: List[S]): List[S]
	def apply(index: Int): A
}


object Nil extends List[Nothing]{
	override def map[B](f: Nothing => B): List[B] = Nil
	override def flatMap[B](f: Nothing => List[B]): List[Nothing] = Nil
	override def head: Nothing = throw new NoSuchElementException
	override def tail: List[Nothing] = throw new NoSuchElementException
	override def apply(index: Int): Nothing = throw new NoSuchElementException
	override def :::[S >: Nothing](input: List[S]): List[S] = Nil
}

class Cons[T](override val head: T, override val tail: List[T]) extends List[T] {

	override def apply(index: Int): T = {
		def recApply(cursor: Int, current: List[T]): T = {
			if (cursor == index) return current.head
			recApply(cursor+1, current.tail)
		}

		recApply(0, this)
	}

	def map[B](f: T => B): List[B] = {
		def recMap(remaining: List[T], acc: List[B]): List[B] = {
			if (remaining == Nil) return acc
			recMap(remaining.tail, f(remaining.head) :: acc)
		}

		recMap(this, Nil)
	}

		override def :::[S >: T](another: List[S]): List[S] = {
		def recConcat(left: List[S], acc: List[S]): List[S] = {
			if (left == Nil) return acc
			recConcat(left.tail, left.head :: acc)
		}

		recConcat(another, this)
	}

	override def flatMap[B](f: T => List[B]): List[B] = {
		def recFlatmap(remaining: List[T], acc: List[B]): List[B] = {
			if (remaining == Nil) return acc
			recFlatmap(remaining.tail, f(remaining.head) ::: acc)
		}

		recFlatmap(this, Nil)
	}
}
