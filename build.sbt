name := """model-driven-documents"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies += "com.orientechnologies" % "orientdb-graphdb" % "2.1-rc2"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws
)
