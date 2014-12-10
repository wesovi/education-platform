package com.wesovi.ep.core.exception


case class WesoviException private(ex: RuntimeException) extends RuntimeException(ex) {

  var error: WesoviError.Value = _ 

  def this(error: WesoviError.Value, message: String, throwable: Option[Throwable]) = {
    this(new RuntimeException(message, throwable.getOrElse(null)))
    this.error = error
  }

}

object DuplicatedElementException extends RuntimeException{
  def apply(message: String, throwable: Option[Throwable])  = new WesoviException(WesoviError.DuplicatedEelement,message,throwable)
} 

object BadRequestException extends RuntimeException{
  def apply(message: String, throwable: Option[Throwable]) = new WesoviException(WesoviError.BadRequest,message,throwable)
}

object ResourceNotFoundException extends RuntimeException{
  def apply(message: String, throwable: Option[Throwable]) = new WesoviException(WesoviError.ResourceNotFound,message,throwable)
}

object UnAuthorizedUserException extends RuntimeException{
  def apply(message: String, throwable: Option[Throwable]) = new WesoviException(WesoviError.UnAuthorized,message,throwable)
}

object InvalidTokenException extends RuntimeException{
  def apply(message: String, throwable: Option[Throwable]) = new WesoviException(WesoviError.InvalidToken,message,throwable)
}

object CommunicationException extends RuntimeException{
  def apply(message: String, throwable: Option[Throwable]) = new WesoviException(WesoviError.DatabaseCommunicationError,message,throwable)
}

object UnexpectedException extends RuntimeException{
  def apply(message: String, throwable: Option[Throwable]) = new WesoviException(WesoviError.UnexpectedException,message,throwable)
}