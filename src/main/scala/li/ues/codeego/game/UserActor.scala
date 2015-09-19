package li.ues.codeego.game

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout 

import scala.concurrent.duration._
import scala.collection.mutable._
import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.{Await, Future}
import scala.util._

class UserActor(user: User) extends Actor {
  def receive = {
    case Live => sender() ! user
    
    case Tick =>
    
    case Whoami => sender() ! user
    
    case Persist =>
    
    case Die => sender() ! "see you soon"
  }
}