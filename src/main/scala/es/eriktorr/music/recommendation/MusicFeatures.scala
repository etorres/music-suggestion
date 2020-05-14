package es.eriktorr.music.recommendation

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
