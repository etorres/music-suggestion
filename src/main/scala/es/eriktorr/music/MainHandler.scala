package es.eriktorr.music

import java.io.{InputStream, OutputStream}

import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

import scala.io.Source

sealed case class MainRequest(key: Option[String])
sealed case class MainResponse(message: String)

object JsonProtocol extends DefaultJsonProtocol {
  implicit val requestFormat: RootJsonFormat[MainRequest] = jsonFormat1(MainRequest)
  implicit val responseFormat: RootJsonFormat[MainResponse] = jsonFormat1(MainResponse)
}

final class MainHandler extends RequestStreamHandler with Logging {
  override def handleRequest(input: InputStream, output: OutputStream, context: Context): Unit = {
    import JsonProtocol._
    import spray.json._

    val request = Source.fromInputStream(input).mkString.parseJson.convertTo[MainRequest]

    logger.info(s"Request: ${request.toString}")

    val response = MainResponse(message = s"Hello ${request.key.getOrElse("guest")}!")
    val outputString = response.toJson.compactPrint

    output.write(outputString.toCharArray.map(_.toByte))
  }
}
