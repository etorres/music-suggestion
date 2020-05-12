package es.eriktorr.music

import com.typesafe.config.ConfigFactory
import es.eriktorr.music.unitspec.UnitSpec
import org.scalatest.BeforeAndAfterAll

class ApplicationContextLoaderSpec extends UnitSpec with BeforeAndAfterAll {
  "Application context loader" should "load configuration properties from resource file" in {
    ApplicationContextLoader.applicationContextFrom(
      ConfigFactory.parseResources("application-test.conf")
    ) shouldBe ApplicationContext(spotifyConfig =
      SpotifyConfig(
        SpotifyCredentials(clientId = "client-id", clientSecret = "client-secret"),
        SpotifyEndpoints(
          authorization = "http://localhost:17080/api/token",
          recentlyPlayed = "http://localhost:17080/v1/me/player/recently-played"
        ),
        refreshToken =
          "BQAT3p9k5N8ZkdWKbNT1iDnjZeCkNa7xDGmF7LnsV4uR1RBqXe-3GhJd2tpBAT2MxXbV1ALVItH0VtSG4m1_cjSuNNew_P0aRwHD25qdVh6ID_OjpL3h4UClez7pX3UutWCgZAm22"
      )
    )
  }
}
