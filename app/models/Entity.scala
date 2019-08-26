package models

import models.Tables.UserRow
import play.api.mvc.Request

object Entity {


  case class JWTToken(uid: Int, email: String, ntf: Long)

  case class LoginResponse(timeOut: Long, token: String, uid: Int, nickname: String)

  case class UserRequest[A](user: UserRow, request: Request[A])

}
