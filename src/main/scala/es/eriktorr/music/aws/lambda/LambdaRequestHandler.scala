package es.eriktorr.music.aws.lambda

import com.amazonaws.services.lambda.runtime.Context
import spray.json.JsonFormat

trait LambdaRequestHandler[I, O] {
  def handle(parameters: Map[String, String], requestBody: I, context: Context)(
    implicit requestJsonFormat: JsonFormat[I],
    responseJsonFormat: JsonFormat[O]
  ): O
}
