package es.eriktorr.music

import spray.json.JsonReader
import sttp.client.{HttpURLConnectionBackend, Identity, NothingT, Request, Response, SttpBackend}

trait SpotifyBackend {
  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  protected def send(
    request: Request[Either[String, String], Nothing]
  ): Identity[Response[Either[String, String]]] = {
    implicit val backend: SttpBackend[Identity, Nothing, NothingT] = HttpURLConnectionBackend()
    request.send()
  }

  protected def decodeJson[T <: SpotifyJson: JsonReader](
    response: Identity[Response[Either[String, String]]]
  ): Either[String, T] = {
    import spray.json._
    response.body.map(_.parseJson.convertTo[T])
  }
}
