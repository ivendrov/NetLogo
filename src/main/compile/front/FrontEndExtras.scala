// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.compile
package front

import org.nlogo.{ core, api, nvm, parse }

trait FrontEndExtras { this: nvm.FrontEndInterface =>

  import nvm.FrontEndInterface.ProceduresMap
  import FrontEnd.tokenizer

  // In the following 3 methods, the initial call to NumberParser is a performance optimization.
  // During import-world, we're calling readFromString over and over again and most of the time
  // the result is a number.  So we try the fast path through NumberParser first before falling
  // back to the slow path where we actually tokenize. - ST 4/7/11

  def readFromString(source: String): AnyRef =
    core.NumberParser.parse(source).right.getOrElse(
      new parse.LiteralParser(null, null, null)
        .getLiteralValue(tokenizer.tokenizeString(source)
          .map(parse.Namer0)))

  def readFromString(source: String, world: api.World, extensionManager: api.ExtensionManager): AnyRef =
    core.NumberParser.parse(source).right.getOrElse(
      FrontEnd.literalParser(world, extensionManager)
        .getLiteralValue(tokenizer.tokenizeString(source)
          .map(parse.Namer0)))

  def readNumberFromString(source: String, world: api.World, extensionManager: api.ExtensionManager): java.lang.Double =
    core.NumberParser.parse(source).right.getOrElse(
      FrontEnd.literalParser(world, extensionManager)
        .getNumberValue(tokenizer.tokenizeString(source)
          .map(parse.Namer0)))

  @throws(classOf[java.io.IOException])
  def readFromFile(currFile: api.File, world: api.World, extensionManager: api.ExtensionManager): AnyRef = {
    val tokens: Iterator[core.Token] =
      new parse.TokenReader(currFile, tokenizer)
        .map(parse.Namer0)
    val result =
      FrontEnd.literalParser(world, extensionManager)
        .getLiteralFromFile(tokens)
    // now skip whitespace, so that the model can use file-at-end? to see whether there are any
    // more values left - ST 2/18/04
    // org.nlogo.util.File requires us to maintain currFile.pos ourselves -- yuck!!! - ST 8/5/04
    var done = false
    while(!done) {
      currFile.reader.mark(1)
      currFile.pos += 1
      val i = currFile.reader.read()
      if(i == -1 || !Character.isWhitespace(i)) {
        currFile.reader.reset()
        currFile.pos -= 1
        done = true
      }
    }
    result
  }

  // used by CommandLine
  def isReporter(s: String, program: api.Program, procedures: ProceduresMap, extensionManager: api.ExtensionManager) =
    try {
      val sp = new StructureParser(
        tokenizer.tokenizeString("to __is-reporter? report " + s + "\nend")
          .map(parse.Namer0),
        None, nvm.StructureResults(program, procedures))
      val results = sp.parse(subprogram = true)
      val namer =
        new Namer(program, procedures ++ results.procedures, extensionManager)
      val proc = results.procedures.values.head
      val tokens = namer.process(results.tokens(proc).iterator, proc)
      tokens.toStream
        .drop(1)  // skip _report
        .map(_.tpe)
        .dropWhile(_ == core.TokenType.OpenParen)
        .headOption
        .exists(reporterTokenTypes)
    }
    catch { case _: api.CompilerException => false }

  private val reporterTokenTypes: Set[core.TokenType] = {
    import core.TokenType._
    Set(OpenBracket, Literal, Ident, Reporter)
  }

}
