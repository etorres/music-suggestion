package es.eriktorr.music.aws.lambda.function

import java.io.{InputStream, OutputStream}

import better.files._
import spray.json.{JsonFormat, _}

import scala.io.Source

trait MessageJsonBinding[I, O] {
  def marshal(obj: O, output: OutputStream)(implicit jsonFormat: JsonFormat[O]): Unit =
    for {
      input <- obj.toJson.compactPrint.inputStream.autoClosed
    } input.pipeTo(output)

  def unmarshal(is: InputStream)(implicit jsonFormat: JsonFormat[I]): I =
    Source.fromInputStream(is).mkString.parseJson.convertTo[I]
}
