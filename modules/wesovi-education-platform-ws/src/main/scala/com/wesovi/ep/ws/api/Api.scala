package com.wesovi.ep.ws.api

import akka.actor.{ActorLogging, Props, actorRef2Scala}
import akka.io.IO
import com.wesovi.ep.ws.{Core, CoreActors}
import com.wesovi.ep.ws.route.{StudentRoute, SystemRoute}
import com.wesovi.ep.ws.util.ConfigHolder
import spray.can.Http
import spray.http.HttpHeaders.{`Access-Control-Allow-Credentials`, `Access-Control-Allow-Headers`, `Access-Control-Allow-Methods`, `Access-Control-Allow-Origin`}
import spray.http.HttpMethods._
import spray.http.{HttpOrigin, SomeOrigins, StatusCodes}
import spray.httpx.SprayJsonSupport
import spray.httpx.marshalling.ToResponseMarshallable.isMarshallable
import spray.json.DefaultJsonProtocol
import spray.routing.Directive.pimpApply
import spray.routing._
import spray.util.LoggingContext

import scala.concurrent.ExecutionContext.Implicits.global
 

trait CORSSupport extends Directives {
  private val CORSHeaders = List(
    `Access-Control-Allow-Methods`(GET, POST, PUT, DELETE, OPTIONS),
    `Access-Control-Allow-Headers`("Origin, X-Requested-With, Content-Type, Accept, Accept-Encoding, Accept-Language, Host, Referer, User-Agent"),
    `Access-Control-Allow-Credentials`(true)
  )

  def respondWithCORS(origin: String)(routes: => Route) = {
    val originHeader = `Access-Control-Allow-Origin`(SomeOrigins(Seq(HttpOrigin(origin))))
    respondWithHeaders(originHeader :: CORSHeaders) {
      routes ~ options { complete(StatusCodes.OK) }
    }
  }
}

trait Api extends Directives with RouteConcatenation with CORSSupport with ConfigHolder {
  this: CoreActors with Core =>

  val routes =
    respondWithCORS(getOriginDomain) { 
      pathPrefix("api" / "v1") { 
        new StudentRoute().route ~
        new SystemRoute().route
      }
    } 
  
  val rootService = system.actorOf(ApiService.props(getHostname, getPort, routes))
}

object ApiService {
  
  def props(hostname: String, port: Int, routes: Route) = Props(classOf[ApiService], hostname, port, routes)
}

class ApiService(hostname: String, port: Int, route: Route) extends HttpServiceActor with ActorLogging {
  IO(Http)(context.system) ! Http.Bind(self, hostname, port)
  def receive: Receive = runRoute(route)
}

object ApiRoute {
  case class Message(message: String)

  object ApiRouteProtocol extends DefaultJsonProtocol {
    implicit val messageFormat = jsonFormat1(Message)
  }

  object ApiMessages {
    val UnknownException = "Unknown exception"
    val UnsupportedService = "Sorry, provided service is not supported."
  }
}

abstract class ApiRoute(implicit log: LoggingContext) extends Directives with SprayJsonSupport {

  
}
