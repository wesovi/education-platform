package com.wesovi.ep.user.db.document

import akka.util.Timeout
import org.joda.time.DateTime
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.specs2.matcher._
import org.specs2.runner.JUnitRunner

import scala.concurrent.{Await, Future}

@RunWith(classOf[JUnitRunner])
class CreateNewUserSpec extends BaseDocumentSpec with Matchers{
  
  def logger = LoggerFactory.getLogger(classOf[CreateNewUserSpec])
  
  val defaultUserStatus = "Created"
  val defaultUserType = "Student"
  val defaultPicture = "file:/home/user/icorrales/pictures/18293712389127389213"
  
  val defaultUserConnection = UserConnection("ivan.corrales","corrales")
  val defaultAddress = Address("C/Lucas Jordan 2 1B","","Aranjuez","28300","Madrid","Spain","")
  val defaultPerson = Person("Juan",Some("Corrales"),Some(DateTime.now),Some(defaultAddress))
  val defaultCreatorUserGroupRefs = Seq(UserGroupRef("1928282731ABCCC123123","My team is nice",DateTime.now(),null),UserGroupRef("1928285555666CCC123123","My team is nice VII",DateTime.now(),null))
  val defaultGroupRefs = UserGroupRefs(defaultCreatorUserGroupRefs,Seq.empty[UserGroupRef],Seq.empty[UserGroupRef],Seq.empty[UserGroupRef],Seq.empty[UserGroupRef])
  val defaultMetaData = UserMetaData(null,DateTime.now(),null,DateTime.now(),DateTime.now())
  
  def beforeAll() {
    initializeMongoDatabase()
  }

  def afterAll() {  
    
  }
  
  "User" should {
    /**
    "be created successfuly when only required fields are populated" in {
        val userConnection:UserConnection = UserConnection("","")
        var userDocument:UserDocument = UserDocument(None,defaultUserStatus,defaultUserType,defaultPicture,null,null,null,null)
        val userDocumentFuture:Future[UserDocument] = UserDocumentAPI.create(userDocument)
        val userDocumentResponse = Await.result(userDocumentFuture,Timeout(10000).duration)
        userDocumentResponse._id.get should not be null
        userDocumentResponse._id.get should not be None
        userDocumentResponse.userStatus must be_==(defaultUserStatus)
        userDocumentResponse.picture must be_==(defaultPicture)
        userDocumentResponse.userType must be_==(defaultUserType)
        
    }
    **/
    "be created successfuly when both optional and required fields are populated" in {
        val userConnection:UserConnection = UserConnection("","")
        var userDocument:UserDocument = UserDocument(None,defaultUserStatus,defaultUserType,defaultPicture,Some(defaultUserConnection),Some(defaultPerson),Some(defaultGroupRefs),Some(defaultMetaData))
        var userDocumentFuture:Future[UserDocument] = UserDocumentAPI.create(userDocument)
        var userDocumentResponse = Await.result(userDocumentFuture,Timeout(10000).duration)
        userDocumentResponse._id.get should not be null
        userDocumentResponse._id.get should not be None
        userDocumentResponse.userStatus must be_==(defaultUserStatus)
        userDocumentResponse.picture must be_==(defaultPicture)
        userDocumentResponse.userType must be_==(defaultUserType)
        var userDocumentGetFuture:Future[Option[UserDocument]] = UserDocumentAPI.byId(userDocumentResponse._id.getOrElse(null))
        val userDocumentGetResponse = Await.result(userDocumentGetFuture,Timeout(10000).duration).get
        userDocumentResponse._id must be_==(userDocumentGetResponse._id)
        userDocumentResponse.person.get.firstName must be_==(userDocumentGetResponse.person.get.firstName)
        userDocumentResponse.person.get.lastName must be_==(userDocumentGetResponse.person.get.lastName)
        userDocumentResponse.person.get.address.get.city must be_==(userDocumentGetResponse.person.get.address.get.city)
        userDocumentResponse.metaData.get.version must be_==(userDocumentGetResponse.metaData.get.version)
        userDocumentResponse.userConnection.get.username must be_==(userDocumentGetResponse.userConnection.get.username)
        userDocumentResponse.groupRefs.get.invited.size must be_==(userDocumentGetResponse.groupRefs.get.invited.size)

    }
    
  
    /**
    "be created successfuly" in {
      UserDocumentAPI.cleanUp
      val userConnection:UserConnection = UserConnection("","")
      
      var userDocument:UserDocument = UserDocument(None,defaultUserStatus,"userType","picture",Some(userConnection),null,null,null)
      val userDocumentFuture:Future[UserDocument] = UserDocumentAPI.create(userDocument)
      userDocument = Await.result(userDocumentFuture,Timeout(5000).duration) 
      logger.info("userDocument  "+userDocument.toString())
      val userDocumentId = userDocument._id.get
     
      val userDocumentFoundFuture  = UserDocumentAPI.get(userDocumentId)
      userDocument = Await.result(userDocumentFoundFuture,Timeout(50000).duration).get
      
      userDocument._id.get must be_==(userDocumentId)
      userDocument.userStatus must be_==(defaultUserStatus)
     
      val userDocumentAllFuture = UserDocumentAPI.all()
      val userDocuments:List[UserDocument] = Await.result(userDocumentAllFuture,Timeout(20000).duration)
      userDocuments should not be null
      userDocuments should not be Nil
      userDocuments.size must be_==(1)
      
      
    }
    * **/
    
  /**
    "3 users are created in parallel" in{
      UserDocumentAPI.cleanUp
      var userDocumentsInput:Seq[UserDocument]=Seq.newBuilder[UserDocument].result
      val userConnection:UserConnection = UserConnection("","")
      userDocumentsInput=userDocumentsInput:+UserDocument(None,"userStatus","userType",Some("picture"),Some(userConnection),null,null,null)
      userDocumentsInput=userDocumentsInput:+UserDocument(None,"userStatus2","userType",Some("picture"),Some(userConnection),null,null,null)
      userDocumentsInput=userDocumentsInput:+UserDocument(None,"userStatus3","userType",Some("picture"),Some(userConnection),null,null,null)
      println("Going for "+userDocumentsInput.size)
      val userDocumentsOutput = UserDocumentAPI.createBulk(userDocumentsInput)
      logger.info(userDocumentsOutput.toString())
      userDocumentsOutput.size must be_==(userDocumentsInput.size)
      userDocumentsOutput.toSet must be_==(userDocumentsInput.toSet)
      
    }
    * 
    */
  }
  
  
  
  
}