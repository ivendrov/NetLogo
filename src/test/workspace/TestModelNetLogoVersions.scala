// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.workspace

import org.scalatest.FunSuite
import org.nlogo.api.{ FileIO, Version}
import org.nlogo.api.model.ModelReader
import org.nlogo.util.SlowTest
import org.nlogo.nvm.DefaultParserServices
import org.nlogo.compile.front.FrontEnd

class TestModelNetLogoVersions extends FunSuite with SlowTest {
  val paths = ModelsLibrary.getModelPaths ++ ModelsLibrary.getModelPathsAtRoot("extensions")
  for(path <- paths)
    test(path) {
      val version = ModelReader.parseModel(FileIO.file2String(path), new DefaultParserServices(FrontEnd)).version
      assert(Version.compatibleVersion(version))
    }
}
