// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.prim.etc

import org.nlogo.core.Syntax
import org.nlogo.nvm.{ Command, Context }

class _reloadextensions extends Command {
  override def syntax2 =
    Syntax.commandSyntax(
      agentClassString = "OTPL",
      switches = true)
  override def perform(context: Context) {
    workspace.getExtensionManager.reset()
  }
}
