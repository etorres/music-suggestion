package es.eriktorr.music.unitspec

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import org.scalatest.BeforeAndAfterEach

abstract class HttpServerSpec extends UnitSpec with BeforeAndAfterEach {
  private[this] val wireMockServer = new WireMockServer(
    options().port(17443)
  )

  override protected def beforeEach(): Unit = {
    super.beforeEach()
    wireMockServer.start()
  }

  override protected def afterEach(): Unit = {
    wireMockServer.stop()
    super.afterEach()
  }

  // Needed to support calls to the fluent interface in the Java API: WireMockServer
  @SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
  protected def stubFor(mappingBuilder: MappingBuilder): Unit =
    wireMockServer.stubFor(mappingBuilder)
}
