import scala.util.parsing.combinator.JavaTokenParsers

object Main {
  // Our model to represent s-expressions
  sealed trait SExpr

  case class SList(list: List[SExpr]) extends SExpr

  case class SSym(symbol: String) extends SExpr

  case class SNum(num: Int) extends SExpr

  object SList {
    // Allows us to build SLists using variadic args: SList(expr1, expr2, ...)
    def apply(exprs: SExpr*): SList = SList(exprs.toList)
  }

  object Parser extends JavaTokenParsers {
    def parseInput(input: String): Option[SExpr] = parse(sexpr, input) match {
      case Success(sexpr, _) => Some(sexpr)
      case _                 => None
    }

    def sexpr: Parser[SExpr] = ???

    def slist: Parser[SList] = ???

    def symbol: Parser[SSym] = """[^()"\s]""".r ^^ SSym

    def num: Parser[SNum] = ???
  }

  object Interpreter {
    def add(args: List[SExpr]): SNum = SNum(
      args
        .collect({ case SNum(num) =>
          num
        })
        .sum
    )

    def eval(exprs: SExpr*): SExpr = eval(SList(exprs.toList))

    def eval(l: SList): SExpr = ???
  }

  def main(args: Array[String]): Unit = {
    val input = args(0)

    println(s"Parsing and evaluating: $input")

    Parser.parseInput(input).map(Interpreter.eval(_)).map(_.render()) match {
      case Some(value) => println(s"Result: $value")
      case None        => throw new Error("Something went horribly wrong...")
    }
  }

  implicit class Render(exp: SExpr) {
    def render(): String = exp match {
      case SList(list)  => s"(${list.map(_.render()).mkString(" ")})"
      case SSym(symbol) => symbol
      case SNum(num)    => num.toString
    }
  }
}
