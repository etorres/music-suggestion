package es.eriktorr.music.greetings

import java.io.{InputStream, OutputStream}

import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import es.eriktorr.music.aws.lambda.function.MessageJsonBinding
import spray.json.{RootJsonFormat, _}

sealed case class GreetingsRequest(name: Option[String])
sealed case class GreetingsResponse(message: String)

object JsonProtocol extends DefaultJsonProtocol {
  implicit val requestFormat: RootJsonFormat[GreetingsRequest] = jsonFormat1(GreetingsRequest)
  implicit val responseFormat: RootJsonFormat[GreetingsResponse] = jsonFormat1(GreetingsResponse)
}

class GuestGreeter
    extends RequestStreamHandler
    with MessageJsonBinding[GreetingsRequest, GreetingsResponse] {
  override def handleRequest(input: InputStream, output: OutputStream, context: Context): Unit = {
    import JsonProtocol._

    val message = unmarshal(input).name match {
      case Some(name) => s"Hi $name!"
      case None => "Hi guest!"
    }

    marshal(GreetingsResponse(message), output)
  }
}
