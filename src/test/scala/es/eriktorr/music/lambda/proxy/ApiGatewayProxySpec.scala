package es.eriktorr.music.lambda.proxy

import com.amazonaws.services.lambda.runtime.events.{
  APIGatewayProxyRequestEvent,
  APIGatewayProxyResponseEvent
}
import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import es.eriktorr.music.aws.lambda.proxy.{ApiGatewayProxy, ApiGatewayRequestHandler}
import es.eriktorr.music.recommendation.{
  MusicFeatures,
  MusicPlaylist,
  MusicRecommendation,
  MusicRecommenderJsonProtocol
}
import es.eriktorr.music.unitspec.{LambdaRuntimeStubs, UnitSpec}
import spray.json.JsonFormat

import scala.jdk.CollectionConverters._

class ApiGatewayProxySpec extends UnitSpec with LambdaRuntimeStubs {
  "API gateway proxy" should "forward input parameters to the request handler" in {
    val recommenderProxy = new ApiGatewayProxyFake(
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
      .withHeaders(Map("x-api-gateway-proxy-footprint" -> "ApiGatewayProxyFake").asJava)
      .withBody(
        """{"playlists":[{"name":"My Playlist","service":"spotify","url":"spotify:my_playlist"}]}"""
      )
      .withIsBase64Encoded(false)
  }

  it should "forward default parameters on empty requests" in {
    val recommenderProxy = new ApiGatewayProxyFake(
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
      .withHeaders(Map("x-api-gateway-proxy-footprint" -> "ApiGatewayProxyFake").asJava)
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
  ): ApiGatewayRequestHandlerFake = new ApiGatewayRequestHandlerFake(expected, response)

  final class ApiGatewayRequestHandlerFake(
    expected: (Map[String, String], MusicFeatures),
    response: MusicRecommendation
  ) extends ApiGatewayRequestHandler[MusicFeatures, MusicRecommendation] {
    override def handle(parameters: Map[String, String], request: MusicFeatures, context: Context)(
      implicit requestJsonFormat: JsonFormat[MusicFeatures],
      responseJsonFormat: JsonFormat[MusicRecommendation]
    ): MusicRecommendation =
      expected match {
        case (expectedParameters, expectedRequest) =>
          if (parameters == expectedParameters && request == expectedRequest) response
          else InvalidMusicRecommendation
        case _ => InvalidMusicRecommendation
      }
  }

  final class ApiGatewayProxyFake(private[this] val requestHandler: ApiGatewayRequestHandlerFake)
      extends RequestHandler[APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent]
      with ApiGatewayProxy[MusicFeatures, MusicRecommendation] {

    import MusicRecommenderJsonProtocol._

    override def handleRequest(
      event: APIGatewayProxyRequestEvent,
      context: Context
    ): APIGatewayProxyResponseEvent = handleRequest(requestHandler, event, context)
  }

  private[this] lazy val InvalidMusicRecommendation = MusicRecommendation(
    Seq(MusicPlaylist(name = "Invalid", service = "Invalid", url = "Invalid"))
  )
}
