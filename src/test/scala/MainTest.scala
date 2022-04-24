import org.scalatest.OptionValues
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.language.implicitConversions

class MainTest extends AnyFlatSpec with Matchers with OptionValues {

  import Main._

  private val + = SSym("+")

  private implicit def intToSnum(n: Int): SNum = SNum(n)

  "Parser" should "parse s-expressions" in {
    Parser.parseInput("()").value shouldBe SList(Nil)
    Parser.parseInput("(1 2)").value shouldBe SList(SNum(1), SNum(2))
    Parser.parseInput("(+)").value shouldBe SList(SSym("+"))
    Parser.parseInput("(- (2 (* 3 3)))").value shouldBe SList(
      SSym("-"),
      SList(SNum(2), SList(SSym("*"), SNum(3), SNum(3)))
    )
  }

  "Parser" should "return nothing for invalid input" in {
    Parser.parseInput("((") shouldBe empty
    Parser.parseInput("(()()") shouldBe empty
  }

  "Interpreter" should "support addition" in {
    Interpreter.eval(+, 1, 2) shouldBe SNum(3)
    Interpreter.eval(+, 1) shouldBe SNum(1)
  }
}
