import org.scalatest._
import org.scalatest.Matchers._

class ObserverSpec extends FunSpec with Matchers {
  describe("Something") {
    it("should be equal") {
      1 should equal(1)
    }
  }
}
