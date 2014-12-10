package com.wesovi.ep.exchange.user

import org.joda.time.DateTime

trait Exchange

case class UserExchange(
    val _id:Option[String]=None,
    val userType:String,
    val username:String,
    val password:String,
    val firstName:String,
    val lastName  :String) extends Exchange{
    def id: String = _id.get
    
    def withId(id:Option[String]):UserExchange={
      this.copy(_id=id) 
    }
}

case class PersonDetailsExchange(
  val userId:String,
  val firstName:String,
  val lastName:String,
  val bornDate:DateTime
) extends Exchange

case class UserCreationExchange(
    val id:Option[Long],
    val username:String,
    val password:String)  extends Exchange
    
case class UserIdExchange(
    val id:String) extends Exchange

case class UserUsernameExchange(
    val username:String
)extends Exchange

case class UserCredentialsExchange(
    val username:String,
    val password:String
) extends Exchange

case class ChangePasswordExchange(
    val username:String,
    val oldPassword:String,
    val password:String
) extends Exchange




case class PersonUpdaterRemoteMessage(
  val element:PersonDetailsExchange
) 

case class UserAuthenticationRemoteMessage(
  val element:UserCredentialsExchange
) 
   
case class UserCreationRemoteMessage(
    val element:UserExchange
) 

case class UserFindByIdRemoteMessage(
  val element:UserIdExchange
) 

case class UserFindByUsernameRemoteMessage(
  val element:UserUsernameExchange
) 

case class UserChangePasswordRemoteMessage(
  val element:ChangePasswordExchange
) 

case class UserUpdatePasswordRemoteMessage(
  val element:UserCredentialsExchange
) 
