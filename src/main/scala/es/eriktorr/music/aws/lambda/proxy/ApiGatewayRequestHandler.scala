package es.eriktorr.music.aws.lambda.proxy

import com.amazonaws.services.lambda.runtime.Context
import spray.json.JsonFormat

trait ApiGatewayRequestHandler[I, O] {
  def handle(parameters: Map[String, String], request: I, context: Context)(
    implicit requestJsonFormat: JsonFormat[I],
    responseJsonFormat: JsonFormat[O]
  ): O
}
