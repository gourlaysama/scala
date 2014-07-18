class B {

type T = Int
trait D

// method parameter shadows some other type
def foobar[D](in: D) = in.toString

// class parameter shadows some other type
class A[T](t: T) {
  // method parameter shadows another type parameter
  def bar[T](w: T) = w.toString
 }

// from the ticket: type parameter with a well-known name
class C[Byte](b: Byte)

}
