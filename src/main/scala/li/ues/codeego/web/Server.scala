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
import akka.pattern.ask
import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.{Future, Promise, Await}
import spray.json._
import data.Formats._

trait Service extends HttpService 
  with ServiceHelper
  with routes.Head
  with routes.Tail {

  val myRoute = 
    headRouting ~ 
    pathPrefix("count") {
      get {
        complete((game.Server.core ? game.Count).mapTo[String])
      }
    } ~
    pathPrefix("online") {
      get {
        // ಠ_ಠ
        complete((game.Server.core ? game.WhoseOnline).mapTo[List[game.User]])
      }
    } ~
    path("user" / Segment) { userId =>
      get {
        complete((game.Server.core ? game.WhoIs(userId)).mapTo[game.User])
      }// ~
      //get {
      //  complete((game.Server.core ? game.Kill(userId)).mapTo[String])
      //}
    } ~
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