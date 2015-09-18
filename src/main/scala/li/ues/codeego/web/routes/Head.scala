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

trait Head extends HttpService with ServiceHelper  {
  val headRouting = 
    path("dashboard", redirect("/dashboard/index.html", Found)) ~
    serveDirectory("dashboard", "/web/public/")
}