package com.wesovi.ep.ws.model

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import spray.json.{JsString, JsValue, RootJsonFormat}

/**
 * Created by ivan on 8/12/14.
 */
object DateTimeFormat extends RootJsonFormat[DateTime] {

  val formatter = ISODateTimeFormat.basicDateTimeNoMillis

  def write(obj: DateTime): JsValue = {
    JsString(formatter.print(obj))
  }

  def read(json: JsValue): DateTime = json match {
    case JsString(s) => try {
      formatter.parseDateTime(s)
    }
    catch {
      case t: Throwable => error(s)
    }
    case _ =>
      error(json.toString())
  }

  def error(v: Any): DateTime = {
    val example = formatter.print(0)
    println(f"'$v' is not a valid date value. Dates must be in compact ISO-8601 format, e.g. '$example'")
    null
  }
}
