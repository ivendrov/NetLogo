// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.prim

import org.nlogo.core.Syntax
import org.nlogo.nvm.{ Reporter, Pure, Context }

class _constboolean(_primitiveValue: Boolean) extends Reporter with Pure {

  private[this] val value = Boolean.box(_primitiveValue)
  val primitiveValue = _primitiveValue

  override def syntax =
    Syntax.reporterSyntax(
      ret = Syntax.BooleanType)

  override def toString =
    super.toString + ":" + primitiveValue

  override def report(context: Context): java.lang.Boolean =
    report_1(context)

  def report_1(context: Context): java.lang.Boolean =
    value

  def report_2(context: Context): Boolean =
    _primitiveValue

}
