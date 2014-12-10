import org.sbtidea.SbtIdeaPlugin._

name := "wesovi-education-platform-core"

organization:= "wesovi"

version := "0.0.1-SNAPSHOT"

homepage := Some(url("https://www.wesovi.com"))

startYear := Some(2014)

scalaVersion := "2.11.2"

mainClass := Some("com.wesovi.elearning.api.AccountClientApp")

ideaExcludeFolders += ".idea"

ideaExcludeFolders += ".idea_modules"

unmanagedSourceDirectories in Compile := (scalaSource in Compile).value :: Nil

unmanagedSourceDirectories in Test <<= (sourceDirectory){ src => src / "test" / "scala" :: Nil}

val akkaVersion = "2.3.6"

val scalaTestVersion="2.2.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
  "ch.qos.logback" % "logback-classic" % "1.1.2"
)