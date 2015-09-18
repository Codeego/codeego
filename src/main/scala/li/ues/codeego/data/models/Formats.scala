package li.ues.codeego.data.models

import spray.json._
import DefaultJsonProtocol._
import spray.http._
import spray.httpx.marshalling._
import spray.httpx._
import ContentTypes._
import scala.language.implicitConversions
import java.time.format._
import java.util._

object Formats extends DefaultJsonProtocol with SprayJsonSupport {
  implicit object DateJsonFormat extends RootJsonFormat[Date] {
    override def write(obj: Date) = JsNumber(obj.getTime())
    override def read(json: JsValue) : Date = json match {
      case JsNumber(s) => {
        new Date(s.toLong)
      }
      case _ => throw new DeserializationException("Error info you want here ...")
    }
  }

  implicit val resultErrorFormat = jsonFormat3(ResultError)
  implicit val resultMessageFormat = jsonFormat3(ResultMessage)
  implicit val emptyResultMessageFormat = jsonFormat2(EmptyResult)
  implicit def resultOfFormat[A: JsonFormat] = jsonFormat(ResultOf.apply[A], "result", "errors", "messages")

  implicit def sprayJson[T : JsonFormat]
    (implicit writer: RootJsonWriter[T]) =
      Marshaller.delegate[T, String](`application/json`)
        { v => CompactPrinter(writer.write(v)) }
}