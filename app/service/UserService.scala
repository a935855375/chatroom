package service

import com.google.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile
import models.Tables._
import models.Tables.profile.api._

import scala.concurrent.Future

@Singleton
class UserService @Inject()(@NamedDatabase("server") val dbConfigProvider: DatabaseConfigProvider)

  extends HasDatabaseConfigProvider[JdbcProfile] {

  def getUserByUid(uid: Int): Future[UserRow] = {
    db.run(User.filter(_.id === uid).result.head)
  }
}
