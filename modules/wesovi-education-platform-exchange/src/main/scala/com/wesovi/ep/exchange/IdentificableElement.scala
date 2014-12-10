package com.wesovi.ep.exchange

import com.github.nscala_time.time.Imports._

trait IdentificableElement {
  def uid:Option[String]
}

trait TrackeableElement{
  def creationDate:DateTime
  def lastModificationDate:DateTime
  
}