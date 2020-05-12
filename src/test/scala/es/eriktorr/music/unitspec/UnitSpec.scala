package es.eriktorr.music.unitspec

import better.files.File
import org.scalatest.EitherValues
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

abstract class UnitSpec extends AnyFlatSpec with Matchers with EitherValues {
  val pathToFile: String => String = {
    getClass.getClassLoader.getResource(_).getPath
  }

  val resourceAsString: String => String = (resource: String) => {
    File(pathToFile(resource)).contentAsString
  }
}
