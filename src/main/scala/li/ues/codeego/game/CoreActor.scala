package li.ues.codeego.game

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout 

import scala.concurrent.duration._
import scala.collection.mutable._
import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.{Await, Future}
import scala.util._


/**
 * @TODO(code-style) it's everything wrong in here, nothing is optimized nor written in the right way, it's just a concept
 */
class CoreActor extends Actor {
  implicit val timeout = Timeout(1.second)

  // @TODO(to-check) is this right??? Or it's better to use immutable List on var? This will work in a cluster environment? Using ActorRegistry could be better?
  val signedUsers: Map[String, ActorRef] = Map[String, ActorRef]()

  // yeah yeah it's kinda wrong, but i need to cacheeeeeeeeeeeeee
  var signedUsersCount: String = "0"
  var signedUsersCache: List[User] = List[User]()
  // -- wrong part ends

  def receive = {
    case UserSigned(userActor) => {
      // @TODO(to-check) to avoid timeout, since we are fowarding the message to another actor and waiting answer
      val lastSender = sender()

      // watch actor for death, when actor is dead CoreActor will receive a Terminated letter
      context.watch(userActor)

      userActor ? Live andThen {
        case Success(user: User) => {
          signedUsers += (user.name -> userActor)
          println("[%s] %s signed".format(this.toString, user.name))
          lastSender ! user
        }
      }
    }
    case Count => sender() ! signedUsersCount
    case WhoseOnline => sender() ! signedUsersCache
    case ProcessStats => {
      signedUsersCache = Await.result(Future.sequence(signedUsers.map({
        case (user, actor)  => (actor ? Whoami).mapTo[User]
      }).toList).mapTo[List[User]], Duration.Inf)

      signedUsersCount = signedUsers.size.toString
    }
    
    case WhoIs(username) => {
      // @TODO(to-check) to avoid timeout, since we are fowarding the message to another actor and waiting answer
      val lastSender = sender()

      (signedUsers(username) ? Whoami).mapTo[User] andThen {
        case Success(user) => lastSender ! user
      }
    }

    case Kill(username) => {
      val lastSender = sender()

      (signedUsers(username) ? Die /* can't use PoisonPill since it will kill the actor immediately, we need to ask him first */) andThen {
        case Success(result) => lastSender ! result
      }
    }

    case Terminated(a) => {
      signedUsers.retain({
        case (user, actor) => actor != a
      })
      println("user logged off")
    }

  }
}