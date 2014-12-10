package com.wesovi.ep.ws.util

import java.util.concurrent.atomic.AtomicLong
import scala.util.Random
import java.security.SecureRandom

class IdGenerator {

  val EPOCH = 1288834974657L //Twepoch (4 Nov 2010 01:42:54.657 GMT)
  val RANDOM_BIT = 22
  val MAX_RANDOM = 1  << RANDOM_BIT
  val MAX_TIME   = 1L << (63 - RANDOM_BIT)
  
  private val sr = new SecureRandom()
  private val counterStart = Random.nextInt(MAX_RANDOM)
  private val counter = new AtomicLong(counterStart)
 
  
  
}