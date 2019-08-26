name := """chatroom"""
organization := "com.fan"

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)


scalaVersion := "2.13.0"

libraryDependencies += guice

libraryDependencies += caffeine


// Json
libraryDependencies += "org.julienrf" %% "play-json-derived-codecs" % "6.0.0"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.7.4"


// FRM
libraryDependencies += "com.typesafe.play" %% "play-slick" % "4.0.2"

libraryDependencies += "com.typesafe.slick" %% "slick-codegen" % "3.3.2"

libraryDependencies += "mysql" % "mysql-connector-java" % "6.0.6"

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test


// Jwt
libraryDependencies ++= Seq(
  "commons-codec" % "commons-codec" % "1.10",
  "org.json4s" %% "json4s-native" % "3.6.7",
  "org.json4s" %% "json4s-jackson" % "3.6.7"
)
