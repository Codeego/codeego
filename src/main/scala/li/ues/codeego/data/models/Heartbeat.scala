package li.ues.codeego.data.models

import org.joda.time.DateTime

case class Heartbeat(
  id: Option[Int],
  langId: Int,
  editorId: Int,
  osId: Int,
  projectId: Int,
  branchId: Int,
  userId: Int,
  when: DateTime)