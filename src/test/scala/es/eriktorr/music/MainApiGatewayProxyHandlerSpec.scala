package es.eriktorr.music

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import es.eriktorr.music.unitspec.{LambdaRuntimeStubs, UnitSpec}

class MainApiGatewayProxyHandlerSpec extends UnitSpec with LambdaRuntimeStubs {
  private[this] val handler = new MainApiGatewayProxyHandler

  "Main API gateway proxy handler" should "say hello to a user" in {
    handler
      .handleRequest(aRequest(Some("""{"key":"Jane Doe"}""")), ContextStub)
      .getBody shouldBe """{"message":"Hello Jane Doe!"}"""
  }

  it should "say hello to a guest user" in {
    handler
      .handleRequest(aRequest(None), ContextStub)
      .getBody shouldBe """{"message":"Hello guest!"}"""
  }

  // Needed to support requests with an empty body in the Java API: RequestHandler
  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  private[this] def aRequest(body: Option[String]): APIGatewayProxyRequestEvent =
    new APIGatewayProxyRequestEvent().withBody(body.orNull)
}
