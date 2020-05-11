import sbtassembly.Log4j2MergeStrategy
import sbtrelease.Version

organization := "es.eriktorr"
name := "music-suggestion"
version := "0.1"

scalaVersion := "2.13.2"

resolvers += Resolver.sonatypeRepo("public")

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-lambda-java-events" % "2.2.8",
  "com.amazonaws" % "aws-lambda-java-log4j2" % "1.1.1",
  "io.spray" %% "spray-json" % "1.3.5",
  "com.github.pathikrit" %% "better-files" % "3.8.0",
  "com.softwaremill.sttp.client" %% "core" % "2.1.1",
  "com.iheart" %% "ficus" % "1.4.7",
  "org.scoverage" %% "scalac-scoverage-runtime" % "1.4.1" % Test,
  "org.scalatest" %% "scalatest" % "3.1.1" % Test,
  "com.github.tomakehurst" % "wiremock-jre8" % "2.26.3" % Test,
  "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.8.2" % Test
)

scalacOptions ++= Seq(
  "-encoding",
  "utf8",
  "-Xfatal-warnings",
  "-Xlint",
  "-deprecation",
  "-unchecked"
)

javacOptions ++= Seq(
  "-g:none",
  "-source",
  "11",
  "-target",
  "11",
  "-encoding",
  "UTF-8"
)

scalafmtOnCompile := true

wartremoverErrors ++= Warts.unsafe
wartremoverWarnings ++= Warts.unsafe

coverageMinimum := 80
coverageFailOnMinimum := true

releaseNextVersion := { ver => Version(ver).map(_.bumpMinor.string).getOrElse("Error") }

assemblyJarName in assembly := "music-suggestion.jar"

assemblyMergeStrategy in assembly := {
  case PathList(ps @ _*) if ps.last == "Log4j2Plugins.dat" =>
    Log4j2MergeStrategy.plugincache
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}
