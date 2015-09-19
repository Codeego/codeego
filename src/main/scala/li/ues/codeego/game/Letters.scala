package li.ues.codeego.game

import akka.actor._


case object Tick
case object Count
case object Live
case object Persist
case object WhoseOnline
case object Whoami
case object ProcessStats
case object Die

case class User(name: String)
case class UserSigned(user: ActorRef)
case class UserLogin(username: String)
case class WhoIs(username: String)
case class Kill(username: String)
