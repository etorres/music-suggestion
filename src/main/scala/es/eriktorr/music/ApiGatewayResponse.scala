package es.eriktorr.music

import scala.beans.BeanProperty

// Needed to offer a Java-bean to the request handler
@SuppressWarnings(Array("org.wartremover.warts.DefaultArguments", "org.wartremover.warts.Var"))
case class ApiGatewayResponse(
  @BeanProperty statusCode: Integer,
  @BeanProperty body: String,
  @BeanProperty headers: java.util.Map[String, String],
  @BeanProperty base64Encoded: Boolean = false
)
