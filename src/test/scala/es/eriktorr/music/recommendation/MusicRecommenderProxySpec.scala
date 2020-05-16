package es.eriktorr.music.recommendation

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.{
  APIGatewayProxyRequestEvent,
  APIGatewayProxyResponseEvent
}
import es.eriktorr.music.unitspec.{LambdaRuntimeStubs, UnitSpec}
import spray.json.JsonFormat

import scala.jdk.CollectionConverters._

class MusicRecommenderProxySpec extends UnitSpec with LambdaRuntimeStubs {
  "Music recommender proxy" should "forward input parameters to the request handler" in {
    val recommenderProxy = new MusicRecommenderProxy(
      stubFor(
        (
          Map("playlistName" -> "My Playlist"),
          MusicFeatures(None, None, energetic = Some(true), None, None, None, None, None)
        ),
        MusicRecommendation(playlists =
          Seq(MusicPlaylist(name = "My Playlist", service = "spotify", url = "spotify:my_playlist"))
        )
      )
    )
    recommenderProxy.handleRequest(
      aRequest(Some("""{"energetic":true}"""), Map("playlistName" -> "My Playlist")),
      ContextStub
    ) shouldBe new APIGatewayProxyResponseEvent()
      .withStatusCode(200)
      .withHeaders(Map("x-api-gateway-proxy-footprint" -> "MusicRecommenderProxy").asJava)
      .withBody(
        """{"playlists":[{"name":"My Playlist","service":"spotify","url":"spotify:my_playlist"}]}"""
      )
      .withIsBase64Encoded(false)
  }

  it should "forward default parameters on empty requests" in {
    val recommenderProxy = new MusicRecommenderProxy(
      stubFor(
        (
          Map.empty,
          MusicFeatures(None, None, None, None, None, None, None, None)
        ),
        MusicRecommendation(playlists =
          Seq(
            MusicPlaylist(name = "Default Name", service = "spotify", url = "spotify:my_playlist")
          )
        )
      )
    )
    recommenderProxy.handleRequest(
      aRequest(None, Map.empty),
      ContextStub
    ) shouldBe new APIGatewayProxyResponseEvent()
      .withStatusCode(200)
      .withHeaders(Map("x-api-gateway-proxy-footprint" -> "MusicRecommenderProxy").asJava)
      .withBody(
        """{"playlists":[{"name":"Default Name","service":"spotify","url":"spotify:my_playlist"}]}"""
      )
      .withIsBase64Encoded(false)
  }

  // Needed to support requests with an empty body in the Java API: RequestHandler
  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  private[this] def aRequest(
    body: Option[String],
    pathParameters: Map[String, String]
  ): APIGatewayProxyRequestEvent =
    new APIGatewayProxyRequestEvent()
      .withPathParameters(pathParameters.asJava)
      .withBody(body.orNull)

  private[this] def stubFor(
    expected: (Map[String, String], MusicFeatures),
    response: MusicRecommendation
  ): MusicRecommenderFake = new MusicRecommenderFake(expected, response)

  final class MusicRecommenderFake(
    expected: (Map[String, String], MusicFeatures),
    response: MusicRecommendation
  ) extends MusicRecommenderHandler {
    override def handle(parameters: Map[String, String], request: MusicFeatures, context: Context)(
      implicit requestJsonFormat: JsonFormat[MusicFeatures],
      responseJsonFormat: JsonFormat[MusicRecommendation]
    ): MusicRecommendation = expected match {
      case (expectedParameters, expectedRequest) =>
        if (parameters == expectedParameters && request == expectedRequest) response
        else InvalidMusicRecommendation
      case _ => InvalidMusicRecommendation
    }
  }

  private[this] lazy val InvalidMusicRecommendation = MusicRecommendation(
    Seq(MusicPlaylist(name = "Invalid", service = "Invalid", url = "Invalid"))
  )
}
