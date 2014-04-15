// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.prim

import org.nlogo.core.Syntax
import org.nlogo.api.{ Dump, LogoList, Nobody }
import org.nlogo.nvm.{ Reporter, Pure, Context }

class _const(_value: AnyRef) extends Reporter with Pure {

  def value = _value

  override def syntax2 =
    Syntax.reporterSyntax(
      ret = value match {
        case b: java.lang.Boolean => Syntax.BooleanType
        case d: java.lang.Double => Syntax.NumberType
        case l: LogoList => Syntax.ListType
        case s: String => Syntax.StringType
        case Nobody => Syntax.NobodyType
        case _ => Syntax.WildcardType
      })

  // readable = true so we can distinguish e.g. the number 2 from the string "2"
  override def toString =
    s"${super.toString}:${Dump.logoObject(value, readable = true, exporting = false)}"

  override def report(context: Context): AnyRef =
    report_1(context)

  def report_1(context: Context): AnyRef =
    _value

}
