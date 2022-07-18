package RTJVM

  trait MyLinkedSet[+T]  extends  MySet[T] {
    def head: T
    def tail: MyLinkedSet[T]
    def apply[A>:T](value: A): Boolean
    def flatMap[A](f: T => MyLinkedSet[A]): MyLinkedSet[A]
    override def flatMap[B](f: T => MySet[B]): MySet[B] = throw new RuntimeException("Can only flatten the LinkedSet")
    override def +[A >: T](elem: A): MyLinkedSet[A]
    def ++[A >: T](anotherSet: MyLinkedSet[A]): MyLinkedSet[A]
    override def ++[A >: T](anotherSet: MySet[A]): MySet[T] = throw new RuntimeException("Can only concat with LinkedSet")
  }

  object MyLinkedSet {
      def apply[T](input: T): MyLinkedSet[T] = {
        new NonEmptySet(input, EmptySet)
      }
    }

  object EmptySet extends MyLinkedSet[Nothing] {
    override def head: Nothing = throw new NoSuchElementException
    override def tail: Nothing = throw new NoSuchElementException
    override def contains[T](elem: T): Boolean = false
    override def +[T](elem: T): MyLinkedSet[T] = {
      new NonEmptySet[T](elem, EmptySet)
    }

    override def ++[T](anotherSet: MyLinkedSet[T]): MyLinkedSet[T] = {
      def recAdd(remaining: MyLinkedSet[T], acc: MyLinkedSet[T]): MyLinkedSet[T] = {
        if (remaining == EmptySet) return acc
        recAdd(remaining.tail, acc + remaining.head)
      }
      recAdd(anotherSet, EmptySet)
    }

    override def map[T](f: Nothing => T): MySet[T] = EmptySet
    override def flatMap[T](f: Nothing => MyLinkedSet[T]): MyLinkedSet[T] = EmptySet
    override def filter(predicate: Nothing => Boolean): MyLinkedSet[Nothing] = EmptySet
    override def foreach(f: Nothing => Unit): Unit = {}
    override def apply[T >: Nothing](value: T): Boolean = contains(value)
  }

  class NonEmptySet[T](override val head: T, override val tail: MyLinkedSet[T]) extends MyLinkedSet[T] {

    override def contains[A >: T](elem: A): Boolean = {
      def recSearch(remaining: MyLinkedSet[T]): Boolean = {
        if (remaining == EmptySet) {
          false
        } else if (elem == remaining.head ) {
          true
        } else {
          recSearch(remaining.tail)
        }
      }
      recSearch(this)
    }

    override def +[A>:T](elem: A): MyLinkedSet[A] = {
      if (contains(elem)) return this
      new NonEmptySet[A](elem, this)
    }

    override def ++[A>:T](anotherSet: MyLinkedSet[A]): MyLinkedSet[A] = {
      def recAdd(remaining: MyLinkedSet[A], acc: MyLinkedSet[A]): MyLinkedSet[A] = {
        if (remaining == EmptySet) return acc
        recAdd(remaining.tail, acc + remaining.head)
      }
      recAdd(anotherSet, this)
    }

    override def map[A](f: T => A): MySet[A] = {
      def recMap(remaining: MyLinkedSet[T], acc: MyLinkedSet[A]): MySet[A] = {
        if (remaining == EmptySet) return acc
        recMap(remaining.tail, (acc + f(remaining.head)))
      }
      recMap(this, EmptySet)
    }

    override def flatMap[A](f: T => MyLinkedSet[A]): MyLinkedSet[A] = {
      def recFlatMap(remaining: MyLinkedSet[T], acc: MyLinkedSet[A]): MyLinkedSet[A] = {
        if (remaining == EmptySet) return acc
        recFlatMap(remaining.tail, (acc ++ f(remaining.head)))
      }
      recFlatMap(this, EmptySet)
    }

    override def filter(predicate: T => Boolean): MyLinkedSet[T] = {
      def recFilter(remaining: MyLinkedSet[T], acc: MyLinkedSet[T]): MyLinkedSet[T] = {
        if (remaining == EmptySet) return acc

        if (predicate(remaining.head)) {
          recFilter(remaining.tail, acc + remaining.head)
        } else {
          recFilter(remaining.tail, acc)
        }
      }
      recFilter(this, EmptySet)
    }

    override def foreach(f: T => Unit): Unit = {
      def recForeach(remaining: MyLinkedSet[T]): Unit = {
        if (remaining == EmptySet) return
        f(remaining.head)
        recForeach(remaining.tail)
      }
      recForeach(this)
    }

    override def apply[B](value: B): Boolean =
      contains(value)
  }


