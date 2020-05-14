package es.eriktorr.music.recommendation

import java.io.{InputStream, OutputStream}

import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import es.eriktorr.music.Logging

final class MusicRecommender extends RequestStreamHandler with Logging {
  override def handleRequest(input: InputStream, output: OutputStream, context: Context): Unit = {

    // TODO
  }
}
