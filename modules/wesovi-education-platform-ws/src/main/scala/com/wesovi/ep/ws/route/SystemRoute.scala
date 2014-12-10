package com.wesovi.ep.ws.route

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.util.Failure
import scala.util.Success
import spray.util.LoggingContext
import com.wesovi.ep.ws.api.ApiRoute
import akka.util.Timeout
import akka.actor.ActorRef
import akka.actor.ActorRefFactory
import akka.pattern.ask
import akka.util.Timeout
import spray.http.MediaTypes
import spray.http.StatusCodes
import spray.httpx.marshalling.ToResponseMarshallable.isMarshallable
import spray.routing._
import spray.routing.Directive.pimpApply
import spray.util.LoggingContext

object SystemRoute {

}

class SystemRoute (implicit ec: ExecutionContext, actorRefFactory:ActorRefFactory,log: LoggingContext) extends ApiRoute{
  
  import StudentRoute._
  import com.wesovi.ep.ws.model._
  import com.wesovi.ep.ws.api.ApiRoute._
  import ApiRouteProtocol._
  import ExecutionContext.Implicits.global
  
  implicit val timeout = Timeout(10 seconds)
  
  val route: Route =
  path("system" / "status"){
    get{
      respondWithMediaType(MediaTypes.`application/json`) {
        complete(StatusCodes.Accepted, "ok")
      }
    }
  }
}