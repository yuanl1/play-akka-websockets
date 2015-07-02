package models

import akka.actor.{Props, Actor}

import akka.actor._

object WebSocketActor {
  def props(out: ActorRef) = Props(new WebSocketActor(out))
}

class WebSocketActor(out: ActorRef) extends Actor {

  override def preStart() = {

    println("socket name " + self.toString)
  }

  def receive = {
    case TimeMessage(time) =>
      out ! (time)

  }
}