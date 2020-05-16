package es.eriktorr.music.recommendation

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object MusicRecommenderJsonProtocol extends DefaultJsonProtocol {
  implicit val musicPlaylistFormat: RootJsonFormat[MusicPlaylist] = jsonFormat3(MusicPlaylist)
  implicit val requestFormat: RootJsonFormat[MusicFeatures] = jsonFormat8(MusicFeatures)
  implicit val responseFormat: RootJsonFormat[MusicRecommendation] = jsonFormat1(
    MusicRecommendation
  )
}
