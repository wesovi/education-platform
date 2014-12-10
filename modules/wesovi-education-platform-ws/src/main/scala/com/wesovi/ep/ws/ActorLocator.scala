package com.wesovi.ep.ws

import akka.actor.{ActorRefFactory, ActorSystem, Props}
import com.wesovi.ep.ws.service.StudentService

object ActorLocator {

  def studentService(implicit ctx: ActorRefFactory) = ctx.actorOf(Props[StudentService])
  
  implicit val system = ActorSystem("StudententSystem-1")
  def userRemoteActor(implicit ctx: ActorRefFactory) = system.actorSelection("akka.tcp://UserRemoteSystem@127.0.0.1:20002/user/UserRemoteActor")
  def userStatusRemoteActor(implicit ctx: ActorRefFactory) = system.actorSelection("akka.tcp://UserRemoteSystem@127.0.0.1:20002/user/UserStatusRemoteActor")
  def personRemoteActor(implicit ctx: ActorRefFactory) = system.actorSelection("akka.tcp://PersonRemoteSystem@127.0.0.1:20003/user/PersonRemoteActor")
  
}