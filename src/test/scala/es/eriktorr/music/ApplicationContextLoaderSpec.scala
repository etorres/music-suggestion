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
        "http://localhost:17443/api/token"
      )
    )
  }
}
