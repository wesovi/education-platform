package com.wesovi.ep.user.service


import akka.util.Timeout
import com.wesovi.ep.core.exception.ResourceNotFoundException
import com.wesovi.ep.exchange.user.{PersonDetailsExchange, UserCredentialsExchange, UserExchange}
import com.wesovi.ep.user.db.document.{Person, UserConnection, UserDocument, UserDocumentAPI}
import com.wesovi.ep.user.util.EncryptUtils
import org.slf4j.LoggerFactory

import scala.concurrent.{Await, Future}


trait UserService extends BaseService with EncryptUtils{

  def logger = LoggerFactory.getLogger(classOf[UserService])

  def create(userExchange:UserExchange):UserExchange={
    val person:Person = Person(userExchange.firstName,Some(userExchange.lastName),None,None)
    val connection:UserConnection = UserConnection(userExchange.username,userExchange.password)
    val userDocument:UserDocument = UserDocument(None,"CR",userExchange.userType,"",Some(connection),Some(person),None,None)
    val userDocumentFuture:Future[UserDocument] = UserDocumentAPI.create(userDocument)
    val userDocumentResponse = Await.result(userDocumentFuture,Timeout(10000).duration)
    transformUserDocumentToUserExchange(userDocumentResponse)
  }

  def authenticate(userExchange:UserCredentialsExchange):UserExchange={
    val userDocumentFuture:Future[Option[UserDocument]] = UserDocumentAPI.authenticate(userExchange.username,userExchange.password)
    val userDocumentResponse = Await.result(userDocumentFuture,Timeout(10000).duration)
    transformUserDocumentToUserExchange(userDocumentResponse.getOrElse(null))
  }

  def byId(userEncodedId:String):UserExchange={
    val userId:String = decode(userEncodedId)
    val userDocumentFuture:Future[Option[UserDocument]] = UserDocumentAPI.get(userId)
    val userDocumentResponse = Await.result(userDocumentFuture,Timeout(10000).duration)
    transformUserDocumentToUserExchange(userDocumentResponse.getOrElse(null))
  }

  def updatePersonDetails(personDetailsExchange: PersonDetailsExchange): UserExchange = {
    val userId: String = decode(personDetailsExchange.userId)
    logger.info("Updating person details for user with id "+userId)
    val person:Person = transformPersonDetailsExchangeToPerson(personDetailsExchange)
    logger.info("Person details "+person)
    val userDocumentFuture:Future[Option[UserDocument]] = UserDocumentAPI.updatePersonDetails(userId,person)
    val userDocumentResponse = Await.result(userDocumentFuture,Timeout(10000).duration)
    transformUserDocumentToUserExchange(userDocumentResponse.getOrElse(null))
  }


  def transformPersonDetailsExchangeToPerson(personDetailsExchange:PersonDetailsExchange):Person={
    Person(personDetailsExchange.firstName,Some(personDetailsExchange.lastName),Some(personDetailsExchange.bornDate),None)
  }

  def transformUserDocumentToUserExchange(user:UserDocument):UserExchange={
    if(user==null){
      throw ResourceNotFoundException("User not found.",None)
    }
    UserExchange(Some(encode(user._id.get.stringify)),user.userType,user.userConnection.get.username,user.userConnection.get.password,user.person.get.firstName,user.person.get.lastName.get)
  }



  
}