package controllers

import javax.inject.Inject

import akka.actor.ActorSystem
import models.{WebSocketActor, Stage}
import play.api._
import play.api.mvc._
import play.api.Play.current

class Application @Inject() (stage: Stage) extends Controller {


  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def ws = WebSocket.acceptWithActor[String, String] { request => out =>
    WebSocketActor.props(out)
  }


}
