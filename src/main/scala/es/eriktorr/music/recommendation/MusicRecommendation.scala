package es.eriktorr.music.recommendation

sealed case class MusicPlaylist(name: String, service: String, url: String)

sealed case class MusicRecommendation(playlists: Seq[MusicPlaylist])
