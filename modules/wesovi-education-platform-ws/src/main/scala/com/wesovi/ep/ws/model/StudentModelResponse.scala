package com.wesovi.ep.ws.model

import spray.json.DefaultJsonProtocol

/**
 * Created by ivan on 8/12/14.
 */
sealed trait StudentModelResponse

case class StudentResponse(id:String, username:String,firstName:String,lastName:String) extends StudentModelResponse

object StudentResponse extends DefaultJsonProtocol{
  implicit val studentResponseFormat = jsonFormat4(StudentResponse.apply)
}


