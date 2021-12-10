/*Suppose an arithmetic expression is given as a binary tree. Each leaf is an integer and each internal node is one of '+', '−', '∗', or '/'.
Given the root to such a tree, write a function to evaluate it.
For example, given the following tree:
    *
   / \
  +    +
 / \  / \
3  2  4  5
You should return 45, as it is (3 + 2) * (4 + 5).
*/

trait Tree
case class Branch(func: (Int, Int) => Int, left: Tree, right: Tree) extends Tree
case class Leaf(value: Int) extends Tree

val mytree = Branch( func = _*_,
  Branch(_+_,Leaf(3), Leaf(2)),
  Branch(_+_, Leaf(4), Leaf(5)) )

def calcTree(tree: Tree): Int = {
  tree match {
    case Branch(func, l, r) => func(calcTree(l), calcTree(r))
    case myleaf: Leaf => myleaf.value
  }
}

val result  =calcTree(mytree)
print(result)