package li.ues.codeego.data.collections

import scala.concurrent.ExecutionContext.Implicits.global
import li.ues.codeego._

import data._
import models._
import Context._
import JodaMapper._

import org.joda.time.DateTime

class Heartbeats(tag: Tag) extends Table[Heartbeat](tag, "heartbeats") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def langId = column[Int]("lang_id")
  def editorId = column[Int]("editor_id")
  def osId = column[Int]("os_id")
  def projectId = column[Int]("project_id")
  def branchId = column[Int]("branch_id")
  def userId = column[Int]("user_id")
  def when = column[DateTime]("when")

  def * = (id.?, langId, editorId, osId, projectId, branchId, userId, when) <> (Heartbeat.tupled, Heartbeat.unapply)
}

object Heartbeats extends TableQuery(new Heartbeats(_)) {
  def all = process(Heartbeats.result)
}