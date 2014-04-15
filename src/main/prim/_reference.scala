// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.prim

import org.nlogo.core.{ Syntax, Reference }
import org.nlogo.nvm.{ Reporter, Context }

class _reference(_reference: Reference) extends Reporter {
  reference = _reference
  override def syntax2 =
    Syntax.reporterSyntax(
      ret = Syntax.ReferenceType)
  override def toString =
    super.toString + ":" + reference.kind + "," + reference.vn
  override def report(context: Context): Nothing =
    // we're always supposed to get compiled out of existence
    throw new UnsupportedOperationException
}
