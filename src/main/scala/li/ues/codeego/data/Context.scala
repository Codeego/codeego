package li.ues.codeego

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.Await
import scala.slick.driver.JdbcDriver
import com.mchange.v2.c3p0.ComboPooledDataSource
import com.github.tototoshi.slick._

object Context extends slick.driver.JdbcDriver.API {
  object JodaMapper extends GenericJodaSupport(JdbcDriver)

  // @todo(CRITICAL) redo with application.conf settings for the god sake
  val ds = new ComboPooledDataSource
  ds.setDriverClass("org.postgresql.Driver")
  ds.setUser("codeego")
  ds.setPassword("123456")
  ds.setJdbcUrl("jdbc:postgresql://localhost/codeego")

  implicit val db = Database.forDataSource(ds)

  def process[R](action: DBIOAction[R, NoStream, Nothing]): R = {
    var execution = db.run(action)
    var y = new java.util.Date().getTime()
    execution.onSuccess {
      case x => { 
        println("Query executed in %d ms".format(new java.util.Date().getTime() - y))
      }
    }
    Await.result(execution, Duration.Inf)
  }

}