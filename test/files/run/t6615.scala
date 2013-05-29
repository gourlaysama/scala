object Test {

  def main(args: Array[String]) {
    val s = collection.mutable.ArrayBuffer.fill(5000)("a")
    s(4096) = "b"
    val p = collection.immutable.PagedSeq.fromStrings(s.toList)
    assert(p.slice(4096, 4097).mkString == "b")
  }
}
