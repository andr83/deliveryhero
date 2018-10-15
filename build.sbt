name := "deliveryhero-test"

version := "0.1"

scalaVersion := "2.12.6"

scalacOptions += "-Ypartial-unification"

resolvers += Resolver.bintrayRepo("hseeberger", "maven")

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.3.2",
  "org.typelevel" %% "cats-core" % "1.4.0",
  "com.typesafe.akka" %% "akka-actor" % "2.5.17",
  "com.typesafe.akka" %% "akka-stream" % "2.5.17",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.17" % Test,
  "com.typesafe.akka" %% "akka-http" % "10.1.5",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.5" % Test,
  "de.heikoseeberger" %% "akka-http-circe" % "1.22.0",
  "org.scalatest" %% "scalatest" % "3.0.5" % Test
)

val circeVersion = "0.10.0"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

dockerBaseImage := "openjdk:8-jre-alpine"
dockerExposedPorts := Seq(8080)

enablePlugins(AshScriptPlugin, JavaAppPackaging, DockerPlugin)