package com.wesovi.ep.ws.service

import akka.actor.{Actor, ActorLogging, Props, actorRef2Scala}
import akka.pattern.ask
import akka.util.Timeout
import com.wesovi.ep.core.exception.WesoviException
import com.wesovi.ep.exchange.user._
import com.wesovi.ep.ws.ActorLocator
import com.wesovi.ep.ws.model.{PersonDetailsRequest, StudentAuthenticationRequest, StudentRegistrationRequest, StudentResponse}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success, Try}

object StudentService {
  
  case class StudentRegistrationMessage(studentRegistrationRequest:StudentRegistrationRequest)

  case class StudentAuthenticationMessage(studentAuthenticationRequest:StudentAuthenticationRequest)

  case class UpdatePersonDetailsMessage(studentId:String,personDetailsRequest:PersonDetailsRequest)

  case class GetStudentMessage(studentId:String)

  def props(property:String)=Props(classOf[StudentService],property)
}

class StudentService extends Actor with ActorLogging {
  import com.wesovi.ep.ws.service.StudentService._


  val userType:String = "student"
  
  val personRemoteActor = ActorLocator.personRemoteActor
  val userRemoteActor = ActorLocator.userRemoteActor


  implicit val timeout = Timeout(10 seconds)
  
  def receive = {
    case StudentRegistrationMessage(studentRegistrationRequest) => { 
      val userExchange:UserExchange = createUser(studentRegistrationRequest)
      userExchangeAsStudentResponse(userExchange)
    }
    case StudentAuthenticationMessage(studentAuthenticationRequest) => {
      val userExchange:UserExchange = authenticateUser(studentAuthenticationRequest)
      userExchangeAsStudentResponse(userExchange)
    }
    case GetStudentMessage(studentId) => {
      val userExchange:UserExchange = getUserById(studentId)
      userExchangeAsStudentResponse(userExchange)
    }
    case UpdatePersonDetailsMessage(studentId,personDetailsRequest) => {
      val userExchange:UserExchange = updatePersonDetails(studentId,personDetailsRequest)
      userExchangeAsStudentResponse(userExchange)
    }
  }

  def createUser(studentRegistrationRequest:StudentRegistrationRequest):UserExchange={
    log.info(s"Student with email ${studentRegistrationRequest.email} and password ${studentRegistrationRequest.password}!")
    val userExchange = UserExchange(None,userType,studentRegistrationRequest.email,studentRegistrationRequest.password,studentRegistrationRequest.firstName,studentRegistrationRequest.lastName)
    val future = userRemoteActor ? UserCreationRemoteMessage(userExchange)
    asUserExchange(future)
  }

  def authenticateUser(request:StudentAuthenticationRequest):UserExchange={
    val userExchange = UserCredentialsExchange(request.username,request.password)
    val future = userRemoteActor ? UserAuthenticationRemoteMessage(userExchange)
    asUserExchange(future)
  }

  def getUserById(studentId:String):UserExchange={
    val userIdExchange = UserIdExchange(studentId)
    val future = userRemoteActor ? UserFindByIdRemoteMessage(userIdExchange)
    asUserExchange(future)
  }

  def updatePersonDetails(studentId:String,personDetailsRequest:PersonDetailsRequest):UserExchange={
    val personDetailsExchange = PersonDetailsExchange(studentId,personDetailsRequest.firstName,personDetailsRequest.lastName,personDetailsRequest.bornDate)
    val future = userRemoteActor ? PersonUpdaterRemoteMessage(personDetailsExchange)
    asUserExchange(future)
  }

  def userExchangeAsStudentResponse(userExchange:UserExchange)={
    val response = StudentResponse(userExchange.id,userExchange.username,userExchange.firstName,userExchange.lastName)
    sender() ! response
  }

  def asUserExchange(future:Future[Any]):UserExchange={
    var response:UserExchange = null
    val result: Try[UserExchange] = Await.ready(future, timeout.duration).value.get.asInstanceOf[Try[UserExchange]]
    val resultEither = result match {
      case Success(t) => {
        response = Right(t).b
      }
      case Failure(e) => {
        val wesoviException:WesoviException = Left(e).a.asInstanceOf[WesoviException]
        sender ! akka.actor.Status.Failure(wesoviException)
        throw wesoviException
      }
    }
    log.info("we already have a response from remote actor")
    log.info("Remote actor response is "+response)
    response
  }
}