package com.wesovi.ep.user.db.document

import akka.util.Timeout
import com.wesovi.ep.core.exception.DuplicatedElementException
import com.wesovi.ep.user.db.configuration.MongoDb
import com.wesovi.ep.user.db.document.UserDocument._
import org.slf4j.LoggerFactory
import reactivemongo.bson._
import reactivemongo.core.commands.Count

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.{Failure, Success}


object UserDocumentAPI extends BaseAPI{
  
  def logger = LoggerFactory.getLogger("UserDocumentAPI")
  
  val collectionName:String ="users"

  lazy val collection = MongoDb.getCollection(collectionName)
  
  
  
  def count(query:BSONDocument=BSONDocument()): Future[Int] ={
    collection.db.command(
      Count(
        collection.name,
        Some(query)
      )
    )
  } 
  
  def all():Future[List[UserDocument]]={
    val query = BSONDocument()
    val cursor = collection
      .find(query)
      .cursor[UserDocument]
    cursor.collect[List]()
  }

  def updatePersonDetails(userId:String,personDocument:Person): Future[Option[UserDocument]] = {
    logger.info("Updating person details")
    val selector = BSONDocument("_id" ->BSONObjectID(userId))
    val modifier = BSONDocument("$set" ->BSONDocument("person"->personDocument))
    collection.update(selector,modifier)
    get(userId)
  }
  
  def authenticate(username:String,password:String):Future[Option[UserDocument]]={
    val query = BSONDocument("userConnection"->BSONDocument("username"->username,"password"->password))
    collection.find(query).one[UserDocument]
  }
  
  def get(id:String):Future[Option[UserDocument]]={
    byId(BSONObjectID(id))
  }

  def byUsername(username:String):Future[Seq[UserDocument]]={
    val query = BSONDocument("userConnection.username"->username)
    logger.debug(BSONDocument.pretty(query))
    collection.find(query).cursor[UserDocument].collect[Seq]()
  }
  
  def byId(id:BSONObjectID):Future[Option[UserDocument]]={
    val query = BSONDocument("_id" ->id)
    collection.find(query).one[UserDocument]
  }
  
  def create(userDocument:UserDocument): Future[UserDocument] = {
    val userDocumentGetFuture:Future[Seq[UserDocument]] = byUsername(userDocument.userConnection.get.username)
    val userDocumentGetResponse:Seq[UserDocument] = Await.result(userDocumentGetFuture,Timeout(10000).duration)
    if(userDocumentGetResponse.isEmpty){
      val userDocumentBSONDocument= UserDocumentWriter.write(userDocument)
      collection.insert(userDocumentBSONDocument).map{ error =>
        if(error.ok){
          UserDocumentReader.read(userDocumentBSONDocument)
        }else
          throw error
      }
    }else{
      throw DuplicatedElementException
    }
  }
  
  def createBulk(userDocuments:Seq[UserDocument]):Seq[UserDocument] = {
    val userDocumentsFuture = Future.traverse(userDocuments)(create)
    val userDocumentsOutput:Seq[UserDocument] = Await.result(userDocumentsFuture,5 second)
    userDocumentsOutput
  }
  
  def cleanUp(){
    collection.remove(BSONDocument()).onComplete{
      case Failure(e) => throw e
      case Success(_) => logger.debug("successful!")
    }
    
  }
  
  
  
  /**
  def get(userDocumentId: BSONObjectID):Future[UserDocument] = {
    implicit val reader = UserDocumentReader
    UserDocumentReader.read(collection.find(BSONDocument("_id" -> userDocumentId)).one[UserDocument](reader)
  }
  * **/
  
  
  
}