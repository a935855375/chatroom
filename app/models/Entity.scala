package models

object Entity {


  case class JWTToken(id: Int, username: String, ntf: Long)

  case class LoginResponse(timeOut: Long, token: String, uid: Int, nickname: String)

  case class UserMessage(`type`: Int,
                         time: Long,
                         uid: Int,
                         title: Option[String] = None,
                         content: Option[String] = None,
                         nickname: Option[String] = None,
                         memberCount: Option[Int] = None
                        )

}
