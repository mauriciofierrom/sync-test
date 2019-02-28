import scala.annotation.tailrec
import scala.concurrent.{Future, ExecutionContext}

class Connection(val observer: Observer) {
  import Connection._

  implicit val ec = ExecutionContext.global
  val r = scala.util.Random
  var state: GatheringState = Stopped
  var processed = scala.collection.mutable.ListBuffer.empty[Int]

  def startGathering() =
    state match {
      case Stopped => {
        state = Active

        gathering(1)
        stop()
      }
      case Active => println("Already active")
    }

  def stopGathering() =  {
    state = Stopped
    Thread.sleep(1000)
    println("Processed diff")
    println(processed.diff(observer.done))
  }

  @tailrec
  final def gathering(n: Int): Unit =
    state match {
      case Stopped => {
        println("Gathering stopped")
      }
      case Active => {
        Thread.sleep(50)
        val n = r.nextInt(7)
        observer.onOne(n)
        processed += n
        // println("onOne Called")
        gathering(n + 1)
      }
    }

  def stop() = Future {
    Thread.sleep(3000)
    stopGathering()
  }
}

object Connection {
  sealed abstract class GatheringState
  case object Active extends GatheringState
  case object Stopped extends GatheringState
}
