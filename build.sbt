import sbtassembly.Log4j2MergeStrategy
import sbtrelease.Version

organization := "es.eriktorr"
name := "music-suggestion"
version := "0.1"

scalaVersion := "2.13.2"

resolvers += Resolver.sonatypeRepo("public")

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-lambda-java-events" % "2.2.8",
  "com.amazonaws" % "aws-lambda-java-log4j2" % "1.1.1"
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

//coverageMinimum := 80
//coverageFailOnMinimum := true
//coverageEnabled := true

releaseNextVersion := { ver =>
  Version(ver).map(_.bumpMinor.string).getOrElse("Error")
}

assemblyJarName in assembly := "music-suggestion.jar"

assemblyMergeStrategy in assembly := {
  case PathList(ps @ _*) if ps.last == "Log4j2Plugins.dat" =>
    Log4j2MergeStrategy.plugincache
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}
