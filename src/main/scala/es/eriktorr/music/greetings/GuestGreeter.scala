package es.eriktorr.music.greetings

import java.io.{InputStream, OutputStream}

import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import es.eriktorr.music.Logging
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

import scala.io.Source

sealed case class GreetingsRequest(name: Option[String])
sealed case class GreetingsResponse(message: String)

object JsonProtocol extends DefaultJsonProtocol {
  implicit val requestFormat: RootJsonFormat[GreetingsRequest] = jsonFormat1(GreetingsRequest)
  implicit val responseFormat: RootJsonFormat[GreetingsResponse] = jsonFormat1(GreetingsResponse)
}

final class GuestGreeter extends RequestStreamHandler with Logging {
  override def handleRequest(input: InputStream, output: OutputStream, context: Context): Unit = {
    import JsonProtocol._
    import spray.json._

    val request = Source.fromInputStream(input).mkString.parseJson.convertTo[GreetingsRequest]

    logger.info(s"Request: ${request.toString}")

    val response = GreetingsResponse(message = s"Hi ${request.name.getOrElse("guest")}!")
    val outputString = response.toJson.compactPrint

    output.write(outputString.toCharArray.map(_.toByte))
  }
}
