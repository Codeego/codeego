package li.ues.codeego.game

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout 

import scala.concurrent.duration._
import scala.collection.mutable._
import scala.concurrent.ExecutionContext.Implicits._


case object Tick
case object Count
case object Live
case object Persist

case class User(name: String)
case class UserSigned(user: ActorRef)
case class UserLogin(username: String)

class UserActor(user: User) extends Actor {
  def receive = {
    case Live => println("%s logged-in".format(user.name))
    case Tick => null
    case Persist => null
  }
}

class CoreActor extends Actor {
  // @TODO(to-check) is this right??? Or it's better to use immutable Lit on var?
  val signedUsers: MutableList[ActorRef] = MutableList[ActorRef]()

  def receive = {
    case UserSigned(user) => {
      signedUsers += user
      user ! Live
    }
    case Count => sender() ! signedUsers.length
  }
}

object Server {
  implicit val timeout = Timeout(1.second) 
  
  private val system = ActorSystem("the-game")
  
  val core = system.actorOf(Props(new CoreActor()), name = "the-game-core")

  def boot() = {
    // ignore, it's a pre-delta-version
    (0 to 50).foreach(x => signinUser(new User("test-%d".format(x))))
  }

  def signinUser(user: User) = {
    val actor = system.actorOf(Props(new UserActor(user)))

    // @TODO(to-check) will this both schedules cause collision between them?
    system.scheduler.schedule(
        0.milliseconds,
        30.seconds,
        actor,
        Persist)

    system.scheduler.schedule(
        0.milliseconds,
        5.minutes,
        actor,
        Tick)

    core ! UserSigned(actor)
  }
}