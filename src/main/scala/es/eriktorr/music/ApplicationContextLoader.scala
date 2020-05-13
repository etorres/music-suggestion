package es.eriktorr.music

import com.typesafe.config.{Config, ConfigFactory}
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._

sealed case class SpotifyCredentials(clientId: String, clientSecret: String)

sealed case class SpotifyPlaylistsApi(create: String, addItems: String)

sealed case class SpotifyEndpoints(
  authorization: String,
  recentlyPlayed: String,
  recommendations: String,
  playlists: SpotifyPlaylistsApi
)

sealed case class SpotifyConfig(
  credentials: SpotifyCredentials,
  endpoints: SpotifyEndpoints,
  refreshToken: String
)
sealed case class ApplicationContext(spotifyConfig: SpotifyConfig)

object ApplicationContextLoader {
  def applicationContext(): ApplicationContext =
    applicationContextFrom(ConfigFactory.load())

  def applicationContextFrom(config: Config): ApplicationContext = {
    val spotifyConfig: SpotifyConfig = config.as[SpotifyConfig]("spotify")

    ApplicationContext(spotifyConfig)
  }
}
