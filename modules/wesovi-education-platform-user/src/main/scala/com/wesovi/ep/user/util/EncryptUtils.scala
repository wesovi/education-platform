package com.wesovi.ep.user.util

import org.parboiled.common.Base64

/**
 * Created by ivan on 8/12/14.
 */
trait EncryptUtils {

  def encode(value:String):String={
    val base64:Base64 = Base64.rfc2045()
    base64.encodeToString(value.getBytes(),false)
  }

  def decode(value:String):String={
    val base64:Base64 = Base64.rfc2045()
    val bytes: Array[Byte] = base64.decode(value)
    new String(bytes, "UTF-8")
  }

}
