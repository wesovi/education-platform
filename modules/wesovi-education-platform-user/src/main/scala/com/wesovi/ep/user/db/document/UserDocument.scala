package com.wesovi.ep.user.db.document

import com.wesovi.ep.user.util.BSONUtils
import org.joda.time.DateTime
import org.slf4j.LoggerFactory
import reactivemongo.bson.{BSONArray, BSONDateTime, BSONDocument, BSONDocumentReader, BSONDocumentWriter, BSONObjectID, BSONReader, BSONString, BSONWriter}


 
case class UserDocument(
  _id: Option[BSONObjectID],
  userStatus: String,
  userType: String,
  picture: String,
  userConnection:Option[UserConnection],
  person:Option[Person],
  groupRefs:Option[UserGroupRefs],
  metaData:Option[UserMetaData]
)
  
case class UserMetaData( 
  version:Integer,
  creationDateTime:DateTime,
  lastModificationDateTime:DateTime,
  lastConnectionDateTime:DateTime,
  lastPasswordChangeDateTime:DateTime
)

object UserMetaData{}

case class UserGroupRef(
  id:String,
  name:String,
  timeStamp:DateTime,
  lastSynchronization:DateTime
)

object UserGroupRef

case class UserGroupRefs(
  creator:Seq[UserGroupRef],
  owner:Seq[UserGroupRef],
  member:Seq[UserGroupRef],
  pendingToBeApprobed:Seq[UserGroupRef],
  invited:Seq[UserGroupRef]
)

object UserGroupRefs{}

case class UserConnection(
  username:String,
  password:String
)
object UserConnection{}


case class Address(
  line1:String,
  line2:String,
  city:String,
  zip:String,
  state:String,
  country:String,
  otherDetails:String
)
object Address{}

case class Person ( 
    firstName:String,
    lastName:Option[String],
    bornDate:Option[DateTime],
    address:Option[Address])


trait BaseDocument{
  
  def logger = LoggerFactory.getLogger("WesoviUserDocumentLogger")
}

object UserDocument extends BaseDocument with BSONUtils{
  
  
   implicit object UserMetaDataBSONReader extends BSONReader[BSONDocument,UserMetaData]{
     
     
       override def read(document: BSONDocument): UserMetaData = {
        println("===>"+document.toString())
         UserMetaData(
//          document.getAs[BSONInteger]("version").get.value,
           null,
          BSONDateTimeHandler.read(document.getAs[BSONDateTime]("creationDateTime").getOrElse(null)),
          BSONDateTimeHandler.read(document.getAs[BSONDateTime]("lastModificationDateTime").getOrElse(null)),
          BSONDateTimeHandler.read(document.getAs[BSONDateTime]("lastConnectionDateTime").getOrElse(null)),
          BSONDateTimeHandler.read(document.getAs[BSONDateTime]("lastPasswordChangeDateTime").getOrElse(null))
        )
      }
     }
   
    implicit object UserMetaDataBSONWriter extends BSONWriter[UserMetaData,BSONDocument]{
      override def write(userMetaData: UserMetaData): BSONDocument = {
        BSONDocument(
          "version" -> getBSONIntegerOrNull(userMetaData.version),
          "creationDateTime" -> getBSONDateTimeOrNull(userMetaData.creationDateTime),
          "lastModificationDateTime" -> getBSONDateTimeOrNull(userMetaData.lastModificationDateTime),
          "lasConnectionDateTime" -> getBSONDateTimeOrNull(userMetaData.lastConnectionDateTime),
          "lastPasswordChangeDateTime" -> getBSONDateTimeOrNull(userMetaData.lastPasswordChangeDateTime)
        ) 
      }
    }
  
  
    implicit object UserConnectionBSONReader extends BSONReader[BSONDocument,UserConnection]{
     
       override def read(document: BSONDocument): UserConnection = {
        UserConnection(
          document.getAs[BSONString]("username").get.value,
          document.getAs[BSONString]("password").get.value
        )
      }
     }
    
    implicit object UserConnectionBSONWriter extends BSONWriter[UserConnection,BSONDocument]{
      override def write(userConnection: UserConnection): BSONDocument = {
        BSONDocument(
          "username" -> getBSONStringOrNull(userConnection.username),
          "password" -> getBSONStringOrNull(userConnection.password)
        ) 
      }
    }
    
    implicit object UserGroupRefsBSONReader extends BSONReader[BSONDocument,UserGroupRefs]{
     
       override def read(document: BSONDocument): UserGroupRefs = {
        UserGroupRefs(
          UserGroupRefBSONReader.read(document.getAs[BSONArray]("creator").getOrElse(null)),
          UserGroupRefBSONReader.read(document.getAs[BSONArray]("invited").getOrElse(null)),
          UserGroupRefBSONReader.read(document.getAs[BSONArray]("owner").getOrElse(null)),
          UserGroupRefBSONReader.read(document.getAs[BSONArray]("member").getOrElse(null)),
          UserGroupRefBSONReader.read(document.getAs[BSONArray]("pendingToBeApprobed").getOrElse(null))
        )
      }
     }
    
    implicit object UserGroupRefsBSONWriter extends BSONWriter[UserGroupRefs,BSONDocument]{
      override def write(userGroupRefs: UserGroupRefs): BSONDocument = {
        BSONDocument(
          "creator" -> {
             getBSONArrayOrNull[UserGroupRef](userGroupRefs.creator)(UserGroupRefBSONWriter)
          },
          "invited" -> {
             getBSONArrayOrNull[UserGroupRef](userGroupRefs.invited)(UserGroupRefBSONWriter)
          },
          "owner" -> {
             getBSONArrayOrNull[UserGroupRef](userGroupRefs.owner)(UserGroupRefBSONWriter)
          },
          "member" -> {
             getBSONArrayOrNull[UserGroupRef](userGroupRefs.member)(UserGroupRefBSONWriter)
          },
          "pendingToBeApprobed" -> {
            getBSONArrayOrNull[UserGroupRef](userGroupRefs.pendingToBeApprobed)(UserGroupRefBSONWriter)
          }      
        ) 
      }
    }
    
    implicit object UserGroupRefBSONReader extends BSONReader[BSONArray,Seq[UserGroupRef]]{
     
       override def read(document: BSONArray): Seq[UserGroupRef] = {
        if(document!=null){
           document.values.map{subDocument=>
            UserGroupRef(
              subDocument.asInstanceOf[BSONDocument].getAs[BSONString]("id").get.value,
              subDocument.asInstanceOf[BSONDocument].getAs[BSONString]("name").get.value,
              BSONDateTimeHandler.read(subDocument.asInstanceOf[BSONDocument].getAs[BSONDateTime]("timeStamp").getOrElse(null)),
              BSONDateTimeHandler.read(subDocument.asInstanceOf[BSONDocument].getAs[BSONDateTime]("lastSynchronization").getOrElse(null))
            )
          }.force.toSeq
        }else{
          null
        }
      }
     }
    
    implicit object UserGroupRefBSONWriter extends BSONWriter[UserGroupRef,BSONDocument]{
      override def write(userGroupRef: UserGroupRef): BSONDocument = {
        BSONDocument(
          "id"-> getBSONStringOrNull(userGroupRef.id),
          "name" -> getBSONStringOrNull(userGroupRef.name),
          "timestamp" -> getBSONDateTimeOrNull(userGroupRef.timeStamp),
          "lastSynchronization" -> getBSONDateTimeOrNull(userGroupRef.lastSynchronization)
        ) 
      }
    }
    
    
    implicit object PersonBSONReader extends BSONReader[BSONDocument,Person]{
     
       override def read(document: BSONDocument): Person = {
        Person(
          document.getAs[BSONString]("firstname").get.value,
          Some(document.getAs[BSONString]("lastname").get.value),
          Some(BSONDateTimeHandler.read(document.getAs[BSONDateTime]("bornDate").getOrElse(null))),
          Some(AddressBSONReader.read(document.getAs[BSONDocument]("address").getOrElse(null)))
        )
      }
     }
    
    implicit object PersonBSONWriter extends BSONWriter[Person,BSONDocument]{
      override def write(person: Person): BSONDocument = {
        BSONDocument(
          "firstname" -> getBSONStringOrNull(person.firstName),
          "lastname" -> getBSONStringOrNull(person.lastName.getOrElse(null)),
          "bornDate" -> getBSONDateTimeOrNull(person.bornDate.getOrElse(null)),
          "address" -> (
            if(person.address!=null){
               AddressBSONWriter.write(person.address.getOrElse(null))  
            }else{
              BSONDocument()
            }
          )
        ) 
      }
    }
    
    
    implicit object AddressBSONReader extends BSONReader[BSONDocument,Address]{
     
       override def read(document: BSONDocument): Address = {
         /** if(document!=None){

         Address(
          document.getAs[BSONString]("line1").get.value,
          document.getAs[BSONString]("line2").get.value,
          document.getAs[BSONString]("city").get.value,
          document.getAs[BSONString]("zip").get.value,
          document.getAs[BSONString]("state").get.value,
          document.getAs[BSONString]("country").get.value,
          document.getAs[BSONString]("otherDetails").get.value
        )
        }else{
           null
         }
          **/
           null
      }
     }
    
    implicit object AddressBSONWriter extends BSONWriter[Address,BSONDocument]{
      override def write(address: Address): BSONDocument = {
       if(address==null){
         BSONDocument()
       }else{
        BSONDocument(
          "line1" -> getBSONStringOrNull(address.line1),
          "line2" -> getBSONStringOrNull(address.line2),
          "city" -> getBSONStringOrNull(address.city),
          "zip" -> getBSONStringOrNull(address.zip),
          "state" -> getBSONStringOrNull(address.state),
          "country" -> getBSONStringOrNull(address.country),
          "otherDetails" -> getBSONStringOrNull(address.otherDetails)
        ) 
        }
      }
    }
    
    implicit object UserDocumentReader extends BSONDocumentReader[UserDocument]{
      
      override def read(document: BSONDocument): UserDocument = {
        
        UserDocument( 
          document.getAs[BSONObjectID]("_id"),
          document.getAs[BSONString]("userStatus").get.value, 
          document.getAs[BSONString]("userType").get.value,
          document.getAs[BSONString]("picture").get.value,
          document.getAs[BSONDocument]("userConnection").map{userConnectionDocument=>
            UserConnectionBSONReader.read(userConnectionDocument)
          },
          document.getAs[BSONDocument]("person").map{personDocument=>
            PersonBSONReader.read(personDocument)
          },
          document.getAs[BSONDocument]("groupRefs").map{groupRefsDocument=>
            UserGroupRefsBSONReader.read(groupRefsDocument)
          },
          document.getAs[BSONDocument]("metadata").map{metadataDocument=>
            UserMetaDataBSONReader.read(metadataDocument)
          }
        )
      }
    }
    
    implicit object UserDocumentWriter extends BSONDocumentWriter[UserDocument]{
      override def write(userDocument: UserDocument): BSONDocument = {
        val document:BSONDocument = BSONDocument(
          "_id" -> userDocument._id.getOrElse(BSONObjectID.generate),
          "picture" -> getBSONStringOrNull(userDocument.picture),
          "userStatus" -> getBSONStringOrNull(userDocument.userStatus),
          "userType" -> getBSONStringOrNull(userDocument.userType),
          "userConnection" -> (
            if(userDocument.userConnection!=null){
               UserConnectionBSONWriter.write(userDocument.userConnection.getOrElse(null))  
            }else{
              UserConnectionBSONWriter.write(UserConnection("",""))  
            }
          ),
          "person" -> (
            if(userDocument.person!=None){
               PersonBSONWriter.write(userDocument.person.getOrElse(null))  
            }else{
              BSONDocument()
            } 
          ),
          "groupRefs" ->(
             if(userDocument.groupRefs!=None){
               UserGroupRefsBSONWriter.write(userDocument.groupRefs.getOrElse(null))  
            }else{
              //UserGroupRefsBSONWriter.write(Person("",Option.empty,Option.empty,Option.empty))
               BSONDocument()
            } 
          ),
          "metadata" ->{
            if(userDocument.metaData!=None){
              UserMetaDataBSONWriter.write(userDocument.metaData.getOrElse(null))
            }else{
              BSONDocument()
            }
          }
          
        )
        document
      }
    }
     
 
    
  implicit object BsonFormats {
      import reactivemongo.bson.Macros
      implicit val userDocumentFormat = Macros.handler[UserDocument]
  }


  
}

