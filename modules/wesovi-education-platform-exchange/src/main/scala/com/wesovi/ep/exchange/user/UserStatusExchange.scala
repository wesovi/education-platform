package com.wesovi.ep.exchange.user

case class UserStatusExchange(
  val id:Long,
  val code:String,
  val description:String){

}

case class UserStatusCreationExchange(
  val code:String,
  val description:String
)



case class UserStatusGetAllMessage(
  
)

case class UserStatusCreationMessage(
  val element:UserStatusCreationExchange
)  