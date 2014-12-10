package com.wesovi.ep.exchange.person


import java.util.Date

case class PersonExchange(
    val _id:Option[String]=None,
    val firstName:String,
    val lastName:String,
    val bornDate:Date,
    val address:AddressExchange){
   
    def id: String = _id.get
    
    def withId(id:Option[String]):PersonExchange={
      this.copy(_id=id) 
    }
}

case class AddressExchange(
  var _id:Option[String]=None,
  var line1:String,
  var line2:String,
  var city:String,
  var state:String,
  var zip:String,
  var loc:(String,String),
  var country:String){
  
  def id: String = _id.get
    
    def withId(id:Option[String]):AddressExchange={
      this.copy(_id=id) 
    }
}


case class PersonCreationMessage(
  var element:PersonExchange
)
