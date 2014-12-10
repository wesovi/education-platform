package com.wesovi.ep.ws.model

import com.wesovi.ep.core.validation.EmailValidation
import org.joda.time.DateTime
import spray.json.DefaultJsonProtocol

/**
 * Created by ivan on 8/12/14.
 */
sealed trait StudentModelRequest


case class AddressRequest(line1:String,
                          line2:String,
                          city:String,
                          zip:String,
                          state:String,
                          country:String,
                          otherDetails:String)

object AddressRequest extends DefaultJsonProtocol{
  implicit val addressRequestFormat = jsonFormat7(AddressRequest.apply)
}

case class PersonDetailsRequest(firstName:String,lastName:String,bornDate:DateTime,address:Option[AddressRequest])extends StudentModelRequest

object PersonDetailsRequest extends DefaultJsonProtocol{
  implicit val dateFormat = DateTimeFormat
  implicit val personDetailsRequestFormat = jsonFormat4(PersonDetailsRequest.apply)
}

case class StudentAuthenticationRequest(username:String,password:String)extends StudentModelRequest

object StudentAuthenticationRequest extends DefaultJsonProtocol{
  implicit val studentAuthenticationRequestFormat = jsonFormat2(StudentAuthenticationRequest.apply)
}



case class StudentRegistrationRequest(email:String,password:String,firstName:String,lastName:String) extends StudentModelRequest{

  def validate(){
    assume(email!=null,"password must not be null")
    assume(password!=null,"password must not be null")
    require(!email.isEmpty,"email must not be empty.")
    require(EmailValidation.isValid(email),"Invalid email format.")
    require(!password.isEmpty,"password must not be empty")
    require(password.length>=6 && password.length<=10,"the password length must be between 6 and 10 characters.")
  }

}

object StudentRegistrationRequest extends DefaultJsonProtocol{
  implicit val studentRegistrationRequestFormat = jsonFormat4(StudentRegistrationRequest.apply)
}

