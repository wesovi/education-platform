package com.wesovi.ep.user.db.document

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.specs2.matcher._
import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import org.specs2.specification.AfterEach
import scala.concurrent.Await
import akka.util.Timeout
import org.joda.time.DateTime

@RunWith(classOf[JUnitRunner])
class UserAuthenticationSpec extends BaseDocumentSpec with Matchers{
  
  def logger = LoggerFactory.getLogger(classOf[UserAuthenticationSpec])
  
  val defaultUserStatus = "Created"
  val defaultUserType = "Student"
  val defaultPicture = "file:/home/user/icorrales/pictures/18293712389127389213"
  
  val defaultAddress = Address("C/Lucas Jordan 2 1B","","Aranjuez","28300","Madrid","Spain","")
  val defaultPerson = Person("Juan",Some("Corrales"),Some(DateTime.now),Some(defaultAddress))
  val defaultCreatorUserGroupRefs = Seq(UserGroupRef("1928282731ABCCC123123","My team is nice",DateTime.now(),null),UserGroupRef("1928285555666CCC123123","My team is nice VII",DateTime.now(),null))
  val defaultGroupRefs = UserGroupRefs(defaultCreatorUserGroupRefs,Seq.empty[UserGroupRef],Seq.empty[UserGroupRef],Seq.empty[UserGroupRef],Seq.empty[UserGroupRef])
  val defaultMetaData = UserMetaData(null,DateTime.now(),null,DateTime.now(),DateTime.now())
  
  def beforeAll() {
    //initializeMongoDatabase()
  }

  def afterAll() {  
    
  }
  
  "User" should { 
    
    "be authenticated when the user is registered" in {
        val username:String = DateTime.now().millisOfSecond().toString()
        val password:String ="coco"
        val userConnection:UserConnection = UserConnection(username,password)
        var userDocument:UserDocument = UserDocument(None,defaultUserStatus,defaultUserType,defaultPicture,Some(userConnection),Some(defaultPerson),Some(defaultGroupRefs),Some(defaultMetaData))
        val userDocumentFuture:Future[UserDocument] = UserDocumentAPI.create(userDocument)
        val userDocumentResponse = Await.result(userDocumentFuture,Timeout(10000).duration)
        val userDocumentAuthenticatedResponse = Await.result(UserDocumentAPI.authenticate(username, password),Timeout(10000).duration)
        userDocumentAuthenticatedResponse.get._id must be_==(userDocumentResponse._id) 
    }
	}
}