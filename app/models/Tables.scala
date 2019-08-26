package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.MySQLProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Message.schema ++ User.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Message
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param `type` Database column type SqlType(INT)
   *  @param time Database column time SqlType(BIGINT)
   *  @param uid Database column uid SqlType(INT)
   *  @param title Database column title SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param content Database column content SqlType(VARCHAR), Length(6000,true), Default(None)
   *  @param nickname Database column nickname SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param memberCount Database column member_count SqlType(INT), Default(None) */
  case class MessageRow(id: Int, `type`: Int, time: Long, uid: Int, title: Option[String] = None, content: Option[String] = None, nickname: Option[String] = None, memberCount: Option[Int] = None)
  /** GetResult implicit for fetching MessageRow objects using plain SQL queries */
  implicit def GetResultMessageRow(implicit e0: GR[Int], e1: GR[Long], e2: GR[Option[String]], e3: GR[Option[Int]]): GR[MessageRow] = GR{
    prs => import prs._
    MessageRow.tupled((<<[Int], <<[Int], <<[Long], <<[Int], <<?[String], <<?[String], <<?[String], <<?[Int]))
  }
  /** Table description of table message. Objects of this class serve as prototypes for rows in queries.
   *  NOTE: The following names collided with Scala keywords and were escaped: type */
  class Message(_tableTag: Tag) extends profile.api.Table[MessageRow](_tableTag, Some("chatroom"), "message") {
    def * = (id, `type`, time, uid, title, content, nickname, memberCount) <> (MessageRow.tupled, MessageRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(`type`), Rep.Some(time), Rep.Some(uid), title, content, nickname, memberCount)).shaped.<>({r=>import r._; _1.map(_=> MessageRow.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6, _7, _8)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column type SqlType(INT)
     *  NOTE: The name was escaped because it collided with a Scala keyword. */
    val `type`: Rep[Int] = column[Int]("type")
    /** Database column time SqlType(BIGINT) */
    val time: Rep[Long] = column[Long]("time")
    /** Database column uid SqlType(INT) */
    val uid: Rep[Int] = column[Int]("uid")
    /** Database column title SqlType(VARCHAR), Length(255,true), Default(None) */
    val title: Rep[Option[String]] = column[Option[String]]("title", O.Length(255,varying=true), O.Default(None))
    /** Database column content SqlType(VARCHAR), Length(6000,true), Default(None) */
    val content: Rep[Option[String]] = column[Option[String]]("content", O.Length(6000,varying=true), O.Default(None))
    /** Database column nickname SqlType(VARCHAR), Length(255,true), Default(None) */
    val nickname: Rep[Option[String]] = column[Option[String]]("nickname", O.Length(255,varying=true), O.Default(None))
    /** Database column member_count SqlType(INT), Default(None) */
    val memberCount: Rep[Option[Int]] = column[Option[Int]]("member_count", O.Default(None))
  }
  /** Collection-like TableQuery object for table Message */
  lazy val Message = new TableQuery(tag => new Message(tag))

  /** Entity class storing rows of table User
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param email Database column email SqlType(VARCHAR), Length(255,true)
   *  @param nickname Database column nickname SqlType(VARCHAR), Length(255,true)
   *  @param password Database column password SqlType(VARCHAR), Length(255,true) */
  case class UserRow(id: Int, email: String, nickname: String, password: String)
  /** GetResult implicit for fetching UserRow objects using plain SQL queries */
  implicit def GetResultUserRow(implicit e0: GR[Int], e1: GR[String]): GR[UserRow] = GR{
    prs => import prs._
    UserRow.tupled((<<[Int], <<[String], <<[String], <<[String]))
  }
  /** Table description of table user. Objects of this class serve as prototypes for rows in queries. */
  class User(_tableTag: Tag) extends profile.api.Table[UserRow](_tableTag, Some("chatroom"), "user") {
    def * = (id, email, nickname, password) <> (UserRow.tupled, UserRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(email), Rep.Some(nickname), Rep.Some(password))).shaped.<>({r=>import r._; _1.map(_=> UserRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column email SqlType(VARCHAR), Length(255,true) */
    val email: Rep[String] = column[String]("email", O.Length(255,varying=true))
    /** Database column nickname SqlType(VARCHAR), Length(255,true) */
    val nickname: Rep[String] = column[String]("nickname", O.Length(255,varying=true))
    /** Database column password SqlType(VARCHAR), Length(255,true) */
    val password: Rep[String] = column[String]("password", O.Length(255,varying=true))
  }
  /** Collection-like TableQuery object for table User */
  lazy val User = new TableQuery(tag => new User(tag))
}
