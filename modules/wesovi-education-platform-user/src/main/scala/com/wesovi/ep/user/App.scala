package com.wesovi.ep.user

import akka.actor.ActorSystem
import akka.actor.Props

object UserApp extends App  {
  val system = ActorSystem("UserRemoteSystem")
  val userRemoteActor = system.actorOf(Props[UserRemoteActor], name = "UserRemoteActor")
  //val userStatusRemoteActor = system.actorOf(Props[UserStatusRemoteActor], name = "UserStatusRemoteActor")
  userRemoteActor ! "The userRemoteActor is alive"
 // userStatusRemoteActor ! "The userStatusRemoteActor is alive"
}