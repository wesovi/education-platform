name := "wesovi-education-platform-exchange"

organization:= "wesovi"

version := "0.0.1-SNAPSHOT"

homepage := Some(url("https://www.wesovi.com"))

startYear := Some(2014)

scalaVersion := "2.11.2"

mainClass := Some("com.wesovi.elearning.api.AccountClientApp")

unmanagedSourceDirectories in Compile := (scalaSource in Compile).value :: Nil

unmanagedSourceDirectories in Test <<= (sourceDirectory){ src => src / "test" / "scala" :: Nil}

val akkaVersion = "2.3.6"

val scalaTestVersion="2.2.1"

libraryDependencies ++= Seq(
  "wesovi" %% "wesovi-education-platform-core" % "0.0.1-SNAPSHOT",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
  "ch.qos.logback" % "logback-classic" % "1.1.2"
)

libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "1.4.0"