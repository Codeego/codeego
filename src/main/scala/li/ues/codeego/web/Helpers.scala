package li.ues.codeego.web

import spray.routing.{ PathMatcher, HttpService }
import spray.routing.directives.{ PathDirectives, FileAndResourceDirectives }
import spray.routing._
import spray.json._
import li.ues.codeego._
import web._
import data._
import Formats._
import com.redis._
import spray.http._
import StatusCodes._
import akka.util.Timeout
import scala.concurrent.duration._

object Persistence {
  val client = new RedisClient("localhost", 6379)
  def set[T: JsonFormat](key: String, value: T): T =
    client.set(key, value.toJson.toString) match {
      case true => value
      case _ => throw new Exception("eita")
    }

  def get[T: JsonFormat](key: String): Option[T] = client.get(key) match {
    case Some(e) => {
      Some(e.parseJson.convertTo[T])
    }
    case _ => None
  }
}

trait ServiceHelper {
  this: HttpService =>

  implicit val timeout = Timeout(1.second) 

  def getPath(dir: String) = (new java.io.File(".").getAbsolutePath()) + dir

  def path[L <: shapeless.HList](path: PathMatcher[L], route: spray.routing.Route) = {
    PathDirectives.path(path).happly { implicit x => route }
  }

  def pathPrefix[L <: shapeless.HList](path: String, route: spray.routing.Route) = {
    PathDirectives.pathPrefix(path) { route }
  }

  def serveDirectory(path: String, mount: String)(implicit settings: spray.routing.RoutingSettings) = {
    pathPrefix(path, FileAndResourceDirectives.getFromDirectory(getPath(mount)))
  }

  val completeWithUnauthorized = complete {
    Unauthorized
  }

}