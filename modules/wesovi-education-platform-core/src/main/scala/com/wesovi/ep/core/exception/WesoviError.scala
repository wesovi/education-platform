package com.wesovi.ep.core.exception

object WesoviError extends Enumeration{
  
  type operation = Value
  
  val MissingRequiredAttribute,
      DuplicatedEelement,
      InvalidFormat,
      InvalidToken,
      InvalidCredentials,
      BadRequest,
      ResourceNotFound,
      UnAuthorized,
      DatabaseCommunicationError,
      UnexpectedException
  = Value
}