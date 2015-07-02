package models

import akka.actor.{Props, ActorSystem}
import com.google.inject.{Inject, AbstractModule}
import play.api.Configuration


class StageModule extends AbstractModule {
  def configure() = {
    //Eager singletons can be used to start up a service when an application starts
    bind(classOf[Stage]).asEagerSingleton()
  }
}

class Stage @Inject() (system: ActorSystem, config: Configuration) {
  //Actors can go here
  val timeActor = system.actorOf(TimeActor.props(system, config), "time-actor")
}

case class TimeMessage(time: String)
