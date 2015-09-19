package li.ues.codeego.game

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout 

import scala.concurrent.duration._
import scala.collection.mutable._
import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.{Await, Future}
import scala.util._

object Server {
  implicit val timeout = Timeout(1.second)
  
  private val system = ActorSystem("the-game")
  
  val core = system.actorOf(Props(new CoreActor()), name = "the-game-core")

  def boot() = {
    // ignore, it's a pre-delta-version
    Future.sequence((0 to 50).map(x => signinUser(new User("test-%d".format(x))))) andThen {
      case Success(users) => {
        system.scheduler.schedule(
          0.milliseconds,
          1.minutes,
          core,
          ProcessStats)
      }
    }
  }

  def signinUser(user: User) = {
    val actor = system.actorOf(Props(new UserActor(user)), name = "%s-node".format(user.name))

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

    core ? UserSigned(actor)
  }
}