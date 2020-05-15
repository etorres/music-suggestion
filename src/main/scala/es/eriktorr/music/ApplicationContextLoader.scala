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
  endpoints: SpotifyEndpoints
)

sealed case class User(userId: String, refreshToken: String)

sealed case class UsersConfig(users: Seq[User])

sealed case class ApplicationContext(spotifyConfig: SpotifyConfig, usersConfig: UsersConfig)

object ApplicationContextLoader {
  def applicationContext(): ApplicationContext =
    applicationContextFrom(ConfigFactory.load())

  def applicationContextFrom(config: Config): ApplicationContext = {
    val spotifyConfig: SpotifyConfig = config.as[SpotifyConfig]("spotify")
    val usersConfig: UsersConfig = config.as[UsersConfig]("usersDatabase")

    ApplicationContext(spotifyConfig, usersConfig)
  }
}
