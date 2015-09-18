package li.ues.codeego.web

import li.ues.codeego._
import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._
import StatusCodes._
import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import spray.routing.directives.PathDirectives


trait Service extends HttpService 
  with ServiceHelper
  with routes.Head
  with routes.Tail {

  val myRoute = 
    headRouting ~ 
    //pathPrefix("api") {
    //  
    //} ~
    tailRouting
}

class ServiceActor extends Actor with Service {
  def actorRefFactory = context
  def receive = runRoute(myRoute)
}

object Server {
  def boot() = {
    implicit val system = ActorSystem("server-system")
    val server = system.actorOf(Props[ServiceActor], "server")
    implicit val timeout = Timeout(5.seconds)
    IO(Http) ? Http.Bind(server, interface = "localhost", port = 8080)
  }
}