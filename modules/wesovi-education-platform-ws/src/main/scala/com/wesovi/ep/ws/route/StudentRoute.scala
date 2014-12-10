package com.wesovi.ep.ws.route

import akka.actor.ActorRefFactory
import akka.pattern.ask
import akka.util.Timeout
import com.wesovi.ep.ws.ActorLocator
import com.wesovi.ep.ws.api.ApiRoute
import com.wesovi.ep.core.exception.{WesoviError, WesoviException}
import com.wesovi.ep.ws.service.StudentService.{GetStudentMessage, StudentAuthenticationMessage, StudentRegistrationMessage, UpdatePersonDetailsMessage}
import spray.http.{MediaTypes, StatusCode, StatusCodes}
import spray.routing.Directive.pimpApply
import spray.routing._
import spray.util.LoggingContext

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


 
object StudentRoute {

  
}

class StudentRoute (implicit ec: ExecutionContext, actorRefFactory:ActorRefFactory,log: LoggingContext) extends ApiRoute{
  
  import com.wesovi.ep.ws.api.ApiRoute.ApiRouteProtocol._
  import com.wesovi.ep.ws.model._
  
  
  implicit val timeout = Timeout(10 seconds)

  val studentService = ActorLocator.studentService(actorRefFactory)


  def createStudent(studentRegistrationRequest:StudentRegistrationRequest,ctx:RequestContext)={
    val future:Future[Any] = studentService ? StudentRegistrationMessage(studentRegistrationRequest)
    asStudentResponse(ctx,StatusCodes.Created,future)
  }

  def authenticateStudent(authenticationRequest:StudentAuthenticationRequest,ctx:RequestContext)={
    val future:Future[Any] = studentService ? StudentAuthenticationMessage(authenticationRequest)
    asStudentResponse(ctx,StatusCodes.OK,future)
  }


  def getStudent(studentId:String,ctx:RequestContext)={
    val future:Future[Any] = studentService ? GetStudentMessage(studentId)
    asStudentResponse(ctx,StatusCodes.Found,future)
  }

  def updatePersonDetails(studentId:String,personDetailsRequest:PersonDetailsRequest,ctx:RequestContext)={
    val future:Future[Any] = studentService ? UpdatePersonDetailsMessage(studentId,personDetailsRequest)
    asStudentResponse(ctx,StatusCodes.Found,future)
  }


  def asStudentResponse(ctx:RequestContext,statusCode:StatusCode,future:Future[Any])={
    future.mapTo[StudentResponse] onComplete {
      case Success(data) => {
        ctx.complete(statusCode,data)
      }
      case Failure(wesoviException:WesoviException) => {
        val errorResponse:ErrorResponse = ErrorResponse(String.valueOf(wesoviException.error.id),wesoviException.ex.getMessage())
        ctx.complete(StatusCodes.BadRequest,errorResponse)
      }
      case Failure(exception:Throwable) => {
        val errorResponse:ErrorResponse = ErrorResponse(String.valueOf(WesoviError.UnexpectedException.id),exception.getMessage())
        ctx.complete(StatusCodes.BadRequest,errorResponse)
      }
    }
  }



  val route: Route =
  path("students"){
    post{
		  respondWithMediaType(MediaTypes.`application/json`) {
        entity(as[StudentRegistrationRequest]){
          studentRegistrationRequest => {
            requestContext =>
              createStudent(studentRegistrationRequest,requestContext)
          }
  		  }
		  }
	  }
  }~
  path("students" / "authentication"){
    post{
      respondWithMediaType(MediaTypes.`application/json`) {
        entity(as[StudentAuthenticationRequest]){
          studentAuthenticationRequest => {
            requestContext =>
              authenticateStudent(studentAuthenticationRequest,requestContext)
          }
        }
      }
    }
  }~
  path("students" / Segment){studentId =>
    get{
      respondWithMediaType(MediaTypes.`application/json`) {
        requestContext =>
          getStudent(studentId,requestContext)
      }
    }
  }~
  path("students" / Segment / "person"){studentId =>
    put{
      respondWithMediaType(MediaTypes.`application/json`) {
        entity(as[PersonDetailsRequest]){
          personDetailsRequest => {
            requestContext =>
              updatePersonDetails(studentId,personDetailsRequest,requestContext)
          }
        }
      }
    }
  }
} 