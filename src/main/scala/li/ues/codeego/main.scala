package li.ues.codeego

import scala.concurrent.ExecutionContext.Implicits.global

import li.ues.codeego.DbContext._

case class Heartbeat(
  id: Option[Int],
  langId: Int,
  editorId: Int,
  osId: Int,
  projectId: Int,
  branchId: Int,
  userId: Int,
  when: java.sql.Date)


class Heartbeats(tag: Tag) extends Table[Heartbeat](tag, "heartbeats") {
  def id = column[Int]("id", O.PrimaryKey)
  def langId = column[Int]("lang_id")
  def editorId = column[Int]("editor_id")
  def osId = column[Int]("os_id")
  def projectId = column[Int]("project_id")
  def branchId = column[Int]("branch_id")
  def userId = column[Int]("user_id")
  def when = column[java.sql.Date]("when")

  def * = (id.?, langId, editorId, osId, projectId, branchId, userId, when) <> (Heartbeat.tupled, Heartbeat.unapply)
}

object Heartbeats extends TableQuery(new Heartbeats(_)) {
  def all = process(Heartbeats.result)
}

object Main extends App {
  println(Heartbeats.all)
}