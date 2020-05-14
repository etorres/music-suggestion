package es.eriktorr.music.greetings

import com.amazonaws.services.lambda.runtime.Context
import es.eriktorr.music.Logging
import es.eriktorr.music.aws.lambda.LambdaRequestHandler
import spray.json.JsonFormat

sealed case class GreetingsRequest2(name: Option[String])
sealed case class GreetingsResponse2(message: String)

final class GuestGreeter2
    extends LambdaRequestHandler[GreetingsRequest2, GreetingsResponse2]
    with Logging {
  override def handle(
    parameters: Map[String, String],
    requestBody: GreetingsRequest2,
    context: Context
  )(
    implicit requestJsonFormat: JsonFormat[GreetingsRequest2],
    responseJsonFormat: JsonFormat[GreetingsResponse2]
  ): GreetingsResponse2 = {
    logger.info(s"Request: body=${requestBody.toString}, params=${parameters.toString}")

    val message = parameters.get("name") match {
      case Some(name) => s"Hi $name!"
      case None => "Hi guest!"
    }

    GreetingsResponse2(message)
  }
}
