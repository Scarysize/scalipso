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

    def sexpr: Parser[SExpr] = num | slist | symbol

    def slist: Parser[SList] = "(" ~> sexpr.* <~ ")" ^^ { exprs =>
      SList(exprs)
    }

    def symbol: Parser[SSym] = """[^()"\s]""".r ^^ SSym

    def num: Parser[SNum] = wholeNumber ^^ { s => SNum(s.toInt) }
  }

  object Interpreter {
    def nums: PartialFunction[SExpr, Int] = { case SNum(num) =>
      num
    }

    def add(args: List[SExpr]): SNum = SNum(args.collect(nums).sum)

    def sub(args: List[SExpr]): SNum = args match {
      case SNum(l) :: Nil  => SNum(-l)
      case SNum(l) :: rest => SNum(l - add(rest).num)
      case x               => throw new IllegalArgumentException(s"Can't subtract $x")
    }

    def mul(args: List[SExpr]): SNum = {
      SNum(args.collect(nums).product)
    }

    def eval(exprs: SExpr*): SExpr = eval(SList(exprs.toList))

    def eval(l: SList): SExpr = {
      val e: List[SExpr] = l.list.map {
        case slist: SList => eval(slist)
        case exp          => exp
      }

      e match {
        case SSym("+") :: args => add(args)
        case SSym("-") :: args => sub(args)
        case SSym("*") :: args => mul(args)
        case exprs             => SList(exprs)
      }
    }
  }

  def main(args: Array[String]): Unit = {
    val input = args(0)

    println(s"Parsing and evaluating: $input")

    Parser
      .parseInput(input)
      .collect { case s: SList =>
        Interpreter.eval(s)
      }
      .foreach(result => println(result.render()))
  }

  implicit class Render(exp: SExpr) {
    def render(): String = exp match {
      case SList(list)  => s"(${list.map(_.render()).mkString(" ")})"
      case SSym(symbol) => symbol
      case SNum(num)    => num.toString
    }
  }
}
