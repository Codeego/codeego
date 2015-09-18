package li.ues.codeego.game

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout 

import scala.concurrent.duration._
import scala.collection.mutable._
import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.{Await, Future}

case object Tick
case object Count
case object Live
case object Persist
case object WhoseOnline
case object Whoami
case object ProcessStats

case class User(name: String)
case class UserSigned(user: ActorRef)
case class UserLogin(username: String)

class UserActor(user: User) extends Actor {
  def receive = {
    case Live => println("%s logged-in".format(user.name))
    case Tick => {
      //if(user.name != "test-5")
      //  context.stop(self)
    }
    case Whoami => sender() ! user
    case Persist => {
      //println("user %s called to persist".format(user.name))
    }
  }
}

class CoreActor extends Actor {
  implicit val timeout = Timeout(1.second)

  // @TODO(to-check) is this right??? Or it's better to use immutable List on var? This will work in a cluster environment?
  var signedUsers: List[ActorRef] = List[ActorRef]()
  // yeah yeah it's kinda wrong, but i need to cacheeeeeeeeeeeeee
  var signedUsersCount: String = "0"
  var signedUsersCache: List[User] = List[User]()
  // -- wrong part ends

  def receive = {
    case UserSigned(user) => {
      context.watch(user)
      signedUsers = signedUsers :+ user
      user ! Live
    }
    case Count => sender() ! signedUsersCount
    case WhoseOnline => sender() ! signedUsersCache
    case ProcessStats => {
      signedUsersCache = Await.result(Future.sequence(signedUsers.map(a => (a ? Whoami).mapTo[User])).mapTo[List[User]], Duration.Inf)
      signedUsersCount = signedUsers.length.toString
    }
    case Terminated(a) => {
      signedUsers = signedUsers.drop(signedUsers.indexOf(a))
      println("user logged off")
    }
  }
}

object Server {
  implicit val timeout = Timeout(1.second)
  
  private val system = ActorSystem("the-game")
  
  val core = system.actorOf(Props(new CoreActor()), name = "the-game-core")

  def boot() = {
    // ignore, it's a pre-delta-version
    (0 to 50).foreach(x => signinUser(new User("test-%d".format(x))))
    system.scheduler.schedule(
        0.milliseconds,
        1.minutes,
        core,
        ProcessStats)
  }

  def signinUser(user: User) = {
    val actor = system.actorOf(Props(new UserActor(user)))

    // @TODO(to-check) will this both schedules cause collision between them?
    system.scheduler.schedule(
        30.seconds,
        30.seconds,
        actor,
        Persist)

    system.scheduler.schedule(
        5.seconds,
        5.seconds,
        actor,
        Tick)

    core ! UserSigned(actor)
  }
}