package com.wesovi.ep.ws.util

import com.typesafe.config.ConfigFactory

trait ConfigHolder {
  
  var PROP_HOSTNAME:String="hostname"
  
  var PROP_PORT:String="port"
  
  var PROP_ORIGIN_DOMAIN ="origin.domain"
  
  val config = ConfigFactory.load()
  
  def getHostname():String={
    config.getString(PROP_HOSTNAME)
  }
  
  def getPort():Int={
    config.getInt(PROP_PORT)
  }
  
  def getOriginDomain():String={
    config.getString(PROP_ORIGIN_DOMAIN)
  }
  
}