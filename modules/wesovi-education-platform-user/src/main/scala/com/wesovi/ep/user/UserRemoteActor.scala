package com.wesovi.ep.user

import akka.actor.{Actor, ActorLogging}
import com.wesovi.ep.core.exception.{UnexpectedException, WesoviException}
import com.wesovi.ep.exchange.user._
import com.wesovi.ep.user.service.UserService


import scala.reflect.ClassTag

class UserRemoteActor extends Actor with ActorLogging with UserService{

  def execute[I:ClassTag,O:ClassTag](exchange:I)(function:Function[I,O])={
    try{
      sender().forward(function(exchange))
    }catch{
      case wesoviException:WesoviException=>
        sender ! akka.actor.Status.Failure(wesoviException)
        throw wesoviException
      case exception:Exception =>
        sender ! akka.actor.Status.Failure(UnexpectedException("Unexpected error",Option(exception)))
        throw UnexpectedException("Unexpected error",Option(exception))
    }
  }

  def receive = {
    
    case message:UserCreationRemoteMessage =>{
      log.info(s"Creating a new user with username ${message.element.username} and password ${message.element.password}!")
      execute[UserExchange,UserExchange](message.element)(create)
    }
    case message:UserAuthenticationRemoteMessage =>{
      log.info(s"Authenticating user with username ${message.element.username} and password ${message.element.password}!")
      execute[UserCredentialsExchange,UserExchange](message.element)(authenticate)
    }

    case message:UserFindByIdRemoteMessage =>{
      execute[String,UserExchange](message.element.id)(byId)
    }


    case message:PersonUpdaterRemoteMessage =>{
      log.info(s"Updating person detials for user with id ${message.element.userId}!")
      execute[PersonDetailsExchange,UserExchange](message.element)(updatePersonDetails)
    }
    
  }
  
  
}