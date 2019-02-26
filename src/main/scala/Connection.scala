import scala.annotation.tailrec
import scala.concurrent.{Future, ExecutionContext}

class Connection(val observer: Observer) {
  import Connection.GatheringState

  implicit val ec = ExecutionContext.global
  val r = scala.util.Random
  var state = GatheringState.Stopped
  var processed = scala.collection.mutable.ListBuffer.empty[Int]

  def startGathering() =
    state match {
      case GatheringState.Stopped => {
        state = GatheringState.Active

        gathering(1)
        stop()
      }
      case GatheringState.Active => println("Already active")
    }

  def stopGathering() =  {
    state = GatheringState.Stopped
    Thread.sleep(1000)
    println("Processed diff")
    println(processed.diff(observer.done))
  }

  // @tailrec
  final def gathering(n: Int): Unit = Future {
    state match {
      case GatheringState.Stopped => {
        println("Gathering stopped")
      }
      case GatheringState.Active => {
        Thread.sleep(50)
        val n = r.nextInt(7)
        observer.onOne(n)
        processed += n
        // println("onOne Called")
        gathering(n + 1)
      }
    }
  }

  def stop() = Future {
    Thread.sleep(3000)
    stopGathering()
  }
}

object Connection {
  type GatheringState = GatheringState.Value
  object GatheringState extends Enumeration {
    val Active, Stopped = Value
  }
}
