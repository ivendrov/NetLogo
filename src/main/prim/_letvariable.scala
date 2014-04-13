// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.prim

import org.nlogo.core.Syntax
import org.nlogo.api.Let
import org.nlogo.nvm.{ Context, Reporter }

class _letvariable(private[this] var _let: Let) extends Reporter {

  def this() = this(null)

  // MethodRipper won't let us call a public method from report_1() - ST 7/20/12
  def let = _let
  def let_=(let: Let) = _let = let

  override def syntax =
    Syntax.reporterSyntax(
      ret = Syntax.WildcardType)
  override def toString =
    s"${super.toString}($let)"

  override def report(context: Context): AnyRef =
    report_1(context)
  def report_1(context: Context): AnyRef =
    context.getLet(_let)

}
