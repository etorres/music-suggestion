package es.eriktorr.music.greetings

import java.io.ByteArrayOutputStream

import better.files._
import es.eriktorr.music.unitspec.{LambdaRuntimeStubs, UnitSpec}

class GuestGreeterSpec extends UnitSpec with LambdaRuntimeStubs {
  private[this] val guestGreeter = new GuestGreeter

  "Greetings handler" should "say hi to a user" in {
    request("""{"name":"Jane Doe"}""") shouldBe """{"message":"Hi Jane Doe!"}"""
  }

  it should "say hi to a guest user" in {
    request("{}") shouldBe """{"message":"Hi guest!"}"""
  }

  private[this] def request(input: String): String =
    (for {
      output <- new ByteArrayOutputStream().autoClosed
    } yield {
      guestGreeter.handleRequest(
        input = input.inputStream,
        output = output,
        context = ContextStub
      )
      output
    }).get().toString
}
