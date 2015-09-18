package li.ues.codeego.web.routes

import li.ues.codeego.web._
import spray.routing.{PathMatcher, HttpService}
import spray.routing.directives.{PathDirectives, FileAndResourceDirectives}
import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._
import StatusCodes._
import spray.routing.directives.PathDirectives

trait Tail extends HttpService with ServiceHelper  {
  val tailRouting = 
    path("", redirect("/index.html", Found)) ~
    // @fix(workarounds) it's just a workaround for local tests, maybe it will be served by Nginx
    serveDirectory("", "/web/public/")
}