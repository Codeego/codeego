package li.ues.codeego.data

case class ResultError(message: String, code: String, field: Option[String])
case class ResultMessage(message: String, code: String, field: Option[String])


case class ResultOf[T](result: Option[T], errors: List[ResultError] = List[ResultError](), messages: List[ResultMessage] = List[ResultMessage]()) {
  def addMessage(message: String, code: String, field: Option[String] = None) = new ResultOf(result, errors, messages ::: List(new ResultMessage(message, code, field)))
  def addError(message: String, code: String, field: Option[String] = None) = new ResultOf(result, errors ::: List(new ResultError(message, code, field)), messages)
}

case class EmptyResult(errors: List[ResultError] = List[ResultError](), messages: List[ResultMessage] = List[ResultMessage]()) {
  def addMessage(message: String, code: String, field: Option[String] = None) = new EmptyResult(errors, messages ::: List(new ResultMessage(message, code, field)))
  def addError(message: String, code: String, field: Option[String] = None) = new EmptyResult(errors ::: List(new ResultError(message, code, field)), messages)
}