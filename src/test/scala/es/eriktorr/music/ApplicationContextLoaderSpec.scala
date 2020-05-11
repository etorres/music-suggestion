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
        )
      )
    )
  }
}
