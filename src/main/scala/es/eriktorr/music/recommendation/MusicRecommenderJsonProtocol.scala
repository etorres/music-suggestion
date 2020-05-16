package es.eriktorr.music.recommendation

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

sealed case class MusicFeatures(
  acoustic: Option[Boolean],
  danceable: Option[Boolean],
  energetic: Option[Boolean],
  instrumental: Option[Boolean],
  live: Option[Boolean],
  loud: Option[Boolean],
  spoken: Option[Boolean],
  happy: Option[Boolean]
)

sealed case class MusicPlaylist(name: String, service: String, url: String)

sealed case class MusicRecommendation(playlists: Seq[MusicPlaylist])

object MusicRecommenderJsonProtocol extends DefaultJsonProtocol {
  implicit val musicPlaylistFormat: RootJsonFormat[MusicPlaylist] = jsonFormat3(MusicPlaylist)
  implicit val requestFormat: RootJsonFormat[MusicFeatures] = jsonFormat8(MusicFeatures)
  implicit val responseFormat: RootJsonFormat[MusicRecommendation] = jsonFormat1(
    MusicRecommendation
  )
}
