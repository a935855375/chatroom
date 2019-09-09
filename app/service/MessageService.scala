package service

import com.google.inject.{Inject, Singleton}
import models.Tables
import models.Tables.MessageRow
import play.api.cache.AsyncCacheApi
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile
import models.Tables._
import models.Tables.profile.api._
import play.api.Logger

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class MessageService @Inject()(
                                @NamedDatabase("default")
                                val dbConfigProvider: DatabaseConfigProvider,
                                cache: AsyncCacheApi
                              )
                              (
                                implicit
                                ec: ExecutionContext
                              )

  extends HasDatabaseConfigProvider[JdbcProfile] {

  lazy val logger = Logger(this.getClass)

  def save(message: MessageRow): Unit = {
    db.run(Message += message).map {
      case 1 =>
      case _ => logger.error(s"A message saved failed. content: `$message`")
    }
  }

  def getHistoryMessage(count: Int = 20): Future[Seq[Tables.MessageRow]] = {
    db.run(Message.sortBy(_.id.desc).take(count).result)
  }

  def getPullMessage(from: Int, count: Int = 20): Future[Seq[Tables.MessageRow]] = {
    db.run(Message.filter(_.id < from).sortBy(_.id.desc).take(count).result)
  }
}