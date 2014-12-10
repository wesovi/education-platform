package com.wesovi.ep.user.util

import reactivemongo.bson.BSONNull
import reactivemongo.bson.BSONString
import reactivemongo.bson.BSONValue
import reactivemongo.bson.BSONInteger
import org.joda.time.DateTime
import reactivemongo.bson.BSONHandler
import reactivemongo.bson.BSONDateTime
import reactivemongo.bson.BSONArray
import reactivemongo.bson.BSONWriter
import scala.reflect.ClassTag
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.Producer.valueProducer

trait BSONUtils { 

  def getBSONArrayOrNull[T:ClassTag](elements:Seq[T])(function:BSONWriter[T,BSONDocument]):BSONArray={
     var bsonValue = BSONArray()
      elements.foreach(element=>{
        bsonValue = bsonValue.add(function.write(element))  
      })
      bsonValue
  }
  
  def getBSONIntegerOrNull(value:Integer):BSONValue={
     if(value==null){
       BSONNull
     }else{
       BSONInteger(value)
     }
   }
   
   def getBSONStringOrNull(value:String):BSONValue={
     if(value==null){
       BSONNull
     }else{
       BSONString(value)
     }
   }
   
   def getBSONDateTimeOrNull(value:DateTime):BSONValue={
     if(value==null){
       BSONNull
     }else{
       BSONDateTimeHandler.write(value)
     }
   }
   
   implicit object BSONDateTimeHandler extends BSONHandler[BSONDateTime, DateTime] {
    def read(time: BSONDateTime) = if(time!=null){new DateTime(time.value)}else{null  }
    def write(jdtime: DateTime) = if(jdtime!=null){BSONDateTime(jdtime.getMillis)}else{null}
  }
}