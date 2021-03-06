package es.eriktorr.music.unitspec

import java.net.URI

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import com.typesafe.config.ConfigFactory
import es.eriktorr.music.{ApplicationContextLoader, SpotifyConfig, UsersConfig}
import org.scalatest.BeforeAndAfterEach

abstract class HttpServerSpec extends UnitSpec with BeforeAndAfterEach {
  private[this] val wireMockServer = new WireMockServer(
    options().dynamicPort()
  )

  private[this] def applicationContext() = {
    val context = ApplicationContextLoader
      .applicationContextFrom(
        ConfigFactory.parseResources("application-test.conf")
      )
    val spotifyEndpoints = context.spotifyConfig.endpoints
    context.copy(spotifyConfig =
      context.spotifyConfig.copy(endpoints =
        context.spotifyConfig.endpoints
          .copy(
            authorization = dynamic(spotifyEndpoints.authorization),
            recentlyPlayed = dynamic(spotifyEndpoints.recentlyPlayed),
            recommendations = dynamic(spotifyEndpoints.recommendations),
            playlists = spotifyEndpoints.playlists.copy(
              create = dynamic(spotifyEndpoints.playlists.create),
              addItems = dynamic(spotifyEndpoints.playlists.addItems)
            )
          )
      )
    )
  }

  private[this] def dynamic(endpoint: String): String = {
    val safeEndpoint = endpoint
      .replaceAll("\\{", "%7B")
      .replaceAll("}", "%7D")
    val uri = URI.create(safeEndpoint)
    s"${uri.getScheme}://${uri.getHost}:${wireMockServer.port().toString}${uri.getPath}"
      .replaceAll("%7B", "{")
      .replaceAll("%7D", "}")
  }

  protected def spotifyConfig(): SpotifyConfig = applicationContext().spotifyConfig

  protected def usersConfig(): UsersConfig = applicationContext().usersConfig

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

  protected def verifyGetRequestTo(path: String): Unit =
    wireMockServer.verify(getRequestedFor(urlEqualTo(path)))

  protected def verifyPostRequestTo(path: String): Unit =
    wireMockServer.verify(postRequestedFor(urlPathEqualTo(path)))
}
