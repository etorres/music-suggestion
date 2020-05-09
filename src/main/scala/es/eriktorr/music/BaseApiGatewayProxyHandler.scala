package es.eriktorr.music

import java.io.ByteArrayOutputStream

import better.files._
import com.amazonaws.services.lambda.runtime.events.{
  APIGatewayProxyRequestEvent,
  APIGatewayProxyResponseEvent
}
import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}

import scala.jdk.CollectionConverters._

trait BaseApiGatewayProxyHandler {
  object Defaults {
    val EmptyJson = "{}"
  }

  def handleRequest(
    handler: RequestStreamHandler,
    event: APIGatewayProxyRequestEvent,
    context: Context
  ): APIGatewayProxyResponseEvent = {
    def requestBody = Option(event.getBody).getOrElse(Defaults.EmptyJson)

    val response = (for {
      input <- requestBody.inputStream.autoClosed
      output <- new ByteArrayOutputStream().autoClosed
    } yield {
      handler.handleRequest(input, output, context)
      output.toString()
    }).get()

    val headers = Map("x-custom-response-header" -> "my custom response header value")

    new APIGatewayProxyResponseEvent()
      .withStatusCode(200)
      .withHeaders(headers.asJava)
      .withBody(response)
      .withIsBase64Encoded(false)
  }
}
