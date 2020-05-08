package es.eriktorr.music

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import es.eriktorr.music.unitspec.{LambdaRuntimeStubs, UnitSpec}

class MainApiGatewayProxyHandlerSpec extends UnitSpec with LambdaRuntimeStubs {
  "Main API gateway proxy handler" should "say hello to a user" in {
    val request = new APIGatewayProxyRequestEvent().withBody("""{"key":"Jane Doe"}""")

    val handler = new MainApiGatewayProxyHandler
    val response = handler.handleRequest(request, ContextStub)

    response.getBody shouldBe """{"message":"Hello Jane Doe!"}"""
  }
}
