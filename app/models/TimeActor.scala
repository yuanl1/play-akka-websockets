package models


import javax.inject.Inject
import play.api.Configuration
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import play.api.libs.ws._
import play.api.Play.current

import akka.actor.{Props, ActorSystem, Actor}

import scala.util.control.NonFatal

/**
 * Created by kli on 7/1/15.
 */
class TimeActor (system: ActorSystem, config: Configuration) extends Actor {
  import TimeActor._

  val url = config.getString("url").get

  val holder: WSRequestHolder = WS.url(url).withRequestTimeout(1000)

  //trigger a one-time start message
  override def preStart() = {
    println("time name " + self.toString)
    system.scheduler.scheduleOnce(500 millis, self, Tick)
  }

  // override postRestart so we don't call preStart and schedule a new message
  override def postRestart(reason: Throwable) = {}

  def run() = {
    holder.get().map{ response: WSResponse =>
      println(response.body)
      system.actorSelection("/system/websockets/*/handler") ! TimeMessage(response.body)
    }.recover {
      case NonFatal(e) => //ohh well, next time
    }

    system.scheduler.scheduleOnce(5000 millis, self, Tick)
  }

  def receive = {
    case Tick => run()
  }
}

object TimeActor {
  case object Tick

  def props(system: ActorSystem, config: Configuration): Props = Props(new TimeActor(system, config))

}

