// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.compile
package front

import org.nlogo.{ api, prim }
import Fail._

/**
 *  Connect each _letvariable reference to the _let that defined that variable.
 *  (The connection is through the intermediary of an api.Let object.)
 */

class LetVisitor(usedNames: Map[String, String]) extends DefaultAstVisitor {

  object Environment {
    private var lets = List[List[api.Let]]()
    def push(): Unit =
      lets +:= Nil
    def add(let: api.Let): Unit =
      lets = (let +: lets.head) +: lets.tail
    def get(name: String): Option[api.Let] =
      lets.flatten.find(_.name == name)
    def pop(): Unit =
      lets = lets.tail
  }

  override def visitStatements(stmts: Statements) {
    Environment.push()
    super.visitStatements(stmts)
    Environment.pop()
  }

  override def visitStatement(stmt: Statement) {
    stmt.command match {
      case l: prim._let =>
        val nameToken =
          stmt.args(0).asInstanceOf[ReporterApp]
            .reporter.token
        val name = nameToken.text.toUpperCase
        for (description <- usedNames.get(name))
          exception(s"There is already a $description called $name",
            nameToken)
        if (Environment.get(name).isDefined)
          exception(s"There is already a local variable here called $name",
            nameToken)
        l.let = new api.Let(name)
        Environment.add(l.let)
        stmt.args.tail.foreach(_.accept(this))
      case _ =>
        super.visitStatement(stmt)
    }
  }

  override def visitReporterApp(expr: ReporterApp) {
    expr.reporter match {
      case l: prim._letvariable if l.let == null =>
        val name = l.token.text.toUpperCase
        Environment.get(name) match {
          case Some(let: api.Let) =>
            l.let = let
          case None =>
            val msg = api.I18N.errors.getN(
              "compiler.LocalsVisitor.notDefined", name)
            exception(msg, expr)
        }
      case _ =>
    }
    super.visitReporterApp(expr)
  }

}
