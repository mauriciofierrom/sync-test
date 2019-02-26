object Main extends App {
  override def main(args: Array[String]):Unit = {
    val o = new Observer()
    val c = new Connection(o)

    c.startGathering()
  }
}
