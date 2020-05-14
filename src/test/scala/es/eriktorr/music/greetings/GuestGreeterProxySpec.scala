package es.eriktorr.music.greetings

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import es.eriktorr.music.unitspec.{LambdaRuntimeStubs, UnitSpec}

class GuestGreeterProxySpec extends UnitSpec with LambdaRuntimeStubs {
  private[this] val greeterProxy = new GuestGreeterProxy

  "Greetings API gateway proxy handler" should "say hi to a user" in {
    greeterProxy
      .handleRequest(aRequest(Some("""{"name":"Jane Doe"}""")), ContextStub)
      .getBody shouldBe """{"message":"Hi Jane Doe!"}"""
  }

  it should "say hi to a guest user" in {
    greeterProxy
      .handleRequest(aRequest(None), ContextStub)
      .getBody shouldBe """{"message":"Hi guest!"}"""
  }

  // Needed to support requests with an empty body in the Java API: RequestHandler
  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  private[this] def aRequest(body: Option[String]): APIGatewayProxyRequestEvent =
    new APIGatewayProxyRequestEvent().withBody(body.orNull)
}
