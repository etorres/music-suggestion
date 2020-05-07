package es.eriktorr.music

import scala.beans.BeanProperty

case class Response(@BeanProperty message: String, @BeanProperty request: Request)
