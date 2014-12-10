package com.wesovi.ep.ws.model

import spray.json.DefaultJsonProtocol

case class ErrorResponse(
    code:String,
    description:String
    //message:String,
    //more:String
)

object ErrorResponse extends DefaultJsonProtocol{
  
  implicit val studentResponseFormat = jsonFormat2(ErrorResponse.apply)
  
}