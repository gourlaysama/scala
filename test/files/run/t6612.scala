object Test {
  def main(args: Array[String]) {
    val s = Stream.fill(10000)("a")
    val p = collection.immutable.PagedSeq.fromStrings(s)
    println(p.length)
  }
}
