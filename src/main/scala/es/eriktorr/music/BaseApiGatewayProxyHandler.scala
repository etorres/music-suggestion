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
  def handleRequest(
    handler: RequestStreamHandler,
    event: APIGatewayProxyRequestEvent,
    context: Context
  ): APIGatewayProxyResponseEvent = {
    val response = (for {
      input <- event.getBody.inputStream.autoClosed
      output <- new ByteArrayOutputStream().autoClosed
    } yield {
      handler.handleRequest(input, output, context)
      output.toString()
    }).get()

    val headers = Map("x-custom-response-header" -> "my custom response header value")

    new APIGatewayProxyResponseEvent()
      .withHeaders(headers.asJava)
      .withStatusCode(200)
      .withBody(response)
  }
}
