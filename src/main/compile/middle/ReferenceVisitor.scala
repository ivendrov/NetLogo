// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.compile
package middle

import org.nlogo.{ core, prim }

class ReferenceVisitor extends DefaultAstVisitor {
  override def visitStatement(stmt: Statement) {
    super.visitStatement(stmt)
    val index = stmt.nvmCommand.syntax2.right.indexWhere(_ == core.Syntax.ReferenceType)
    // at present the GIS extension is expecting its _reference arguments not to
    // be removed, so exempt _extern - ST 2/15/11
    if(index != -1 && !stmt.nvmCommand.isInstanceOf[prim._extern]) {
      stmt.nvmCommand.reference =
        stmt.args(index).asInstanceOf[ReporterApp].nvmReporter
          .asInstanceOf[prim._reference].reference
      stmt.removeArgument(index)
    }
  }
}
