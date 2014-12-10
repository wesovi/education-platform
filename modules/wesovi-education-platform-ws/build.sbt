name := "wesovi-education-platform-ws"

organization:= "WesoviLabs"

version := "0.0.1-SNAPSHOT"

homepage := Some(url("https://www.wesovi.com"))

startYear := Some(2014)

scalaVersion := "2.11.2"

mainClass := Some("com.wesovi.ep.MicroServiceSystem")

unmanagedSourceDirectories in Compile := (scalaSource in Compile).value :: Nil

unmanagedSourceDirectories in Test <<= (sourceDirectory){ src => src / "test" / "scala" :: Nil}

val akkaVersion = "2.3.6"

val scalaTestVersion="2.2.1"

val sprayV = "1.3.2"

libraryDependencies ++= Seq(
  "wesovi" %% "wesovi-education-platform-core" % "0.0.1-SNAPSHOT",
  "wesovi" %% "wesovi-education-platform-exchange" % "0.0.1-SNAPSHOT",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
  "io.spray"            %%  "spray-can"     % sprayV,
  "io.spray"            %%  "spray-routing" % sprayV,
  "io.spray" %% "spray-json" % "1.3.1",
  "io.spray"            %%  "spray-testkit" % sprayV  % "test",
  "ch.qos.logback" % "logback-classic" % "1.1.2"
)