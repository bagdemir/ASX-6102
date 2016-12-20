trait Monoid[A] {
  def op(a1: A, a2: A): A
  def zero: A
}

val intAddition: Monoid[Int] = new Monoid[Int] {
  def op(x: Int, y: Int) = x + y
  val zero = 0
}

def foldMap[A, B](as: List[A], m: Monoid[B])(f: A => B): B =
  as.foldLeft(m.zero)((b, a) => m.op(b, f(a)))

foldMap(List(1, 2, 3), intAddition)((a) => a * -1)