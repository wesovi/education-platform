package com.wesovi.ep.ws

import akka.actor.ActorSystem
import com.wesovi.ep.ws.util.ConfigHolder


trait Core {
  implicit def system: ActorSystem
}

trait BootedCore extends Core {
  implicit lazy val system = ActorSystem("microservice-system")

  sys.addShutdownHook(system.shutdown())
}

trait CoreActors extends ConfigHolder {
  this: Core =>
/**
  val exampleService = system.actorOf(ExampleService.props("test property"), "ExampleService")

  val services: Services = Map(
    "example" -> exampleService
  )
  * 
  */
}
