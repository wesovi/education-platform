name := "wesovi-education-platform-user"

organization:= "WesoviLabs"

version := "0.0.1-SNAPSHOT"

homepage := Some(url("https://www.wesovi.com"))

startYear := Some(2014)

scalaVersion := "2.11.2"

mainClass := Some("com.wesovi.ep.user.App")

unmanagedSourceDirectories in Compile := (scalaSource in Compile).value :: Nil

unmanagedSourceDirectories in Test <<= (sourceDirectory){ src => src / "test" / "scala" :: Nil}

evictionWarningOptions in update := EvictionWarningOptions.default.withWarnTransitiveEvictions(false).withWarnDirectEvictions(false).withWarnScalaVersionEviction(false)

val akkaVersion = "2.3.6"

val scalaTestVersion="2.2.1"

val slickVersion = "2.1.0"

libraryDependencies ++= Seq(
  "wesovi" %% "wesovi-education-platform-core" % "0.0.1-SNAPSHOT",
  "wesovi" %% "wesovi-education-platform-exchange" % "0.0.1-SNAPSHOT",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
  "com.typesafe.slick" %% "slick" % slickVersion,
  "ch.qos.logback" % "logback-classic" % "1.1.2",
  "mysql" % "mysql-connector-java" % "latest.release",
  "org.specs2" %% "specs2" % "2.3.13" % "test",
  "com.github.athieriot" %% "specs2-embedmongo" % "0.7.0",
  "org.reactivemongo" %% "reactivemongo" % "0.10.5.0.akka23",
  "org.reactivemongo" %% "reactivemongo-bson-macros" % "0.10.5.akka23-SNAPSHOT",
  "com.github.t3hnar" %% "scala-bcrypt" % "2.4",
  "com.github.simplyscala" %% "scalatest-embedmongo" % "0.2.2" % "test"
)

libraryDependencies += "org.postgresql" % "postgresql" % "9.3-1100-jdbc41"


