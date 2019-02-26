import scala.concurrent.{Future,ExecutionContext}
class Observer { self =>
  import Observer.ObserverState
  implicit val ec = ExecutionContext.global
  private var state = ObserverState.Init
  private var list = scala.collection.mutable.ListBuffer.empty[Int]
  var done = scala.collection.mutable.ListBuffer.empty[Int]

  def onOne(n: Int): Unit = {
    // println(s"The incoming inty: $n. The state: $state")

    // this.synchronized {
      state match {
        case ObserverState.Init => {
          state = ObserverState.Accumulating
          list += n
          enableSetting()
        }
        case ObserverState.Accumulating => {
          // println(s"Accumulating $n")
          println(list.isEmpty)
          list += n
        }
        case ObserverState.Normal => {
          if(!list.isEmpty) {
            list += n
            // println(list)
            list foreach doWithIt
            list.clear()
          } else {
            doWithIt(n)
          }
        }
      }
    // }
  }

  private def doWithIt(n: Int) = {
    done += n
    // println(s"Doing: $n")
  }

  private def enableSetting() = Future {
    println("Waiting on enableSetting")
    Thread.sleep(1000)
    println("Stopped waiting")
    // this.synchronized {
      onOne(88)
      onOne(99)
    // }
    state = ObserverState.Normal
  }
}

object Observer {
  object ObserverState extends Enumeration {
    type ObserverState = Value
    val Init, Accumulating, Normal = Value
  }
}
