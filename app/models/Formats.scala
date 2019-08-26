package models

import akka.util.ByteString
import julienrf.json.derived
import models.Entity.{JWTToken, LoginResponse}
import models.Tables._
import play.api.http.Writeable
import play.api.libs.json.{Json, OFormat, OWrites}

object Formats {
  // 自动将Scala class转换成Json格式的http响应
  implicit def CommonTransformer[A](implicit o: OWrites[A]): Writeable[A] =
    Writeable(a => ByteString.apply(Json.toJson(a).toString()), Some("application/json"))


  implicit lazy val ERROR: OFormat[utils.ErrorFactory.Error] = derived.oformat()

  implicit lazy val USER: OFormat[UserRow] = derived.oformat()

  implicit lazy val JWT_TOKEN: OFormat[JWTToken] = derived.oformat()

  implicit lazy val LOGIN_RESPONSE: OFormat[LoginResponse] = derived.oformat()

  implicit lazy val USER_MESSAGE: OFormat[MessageRow] = derived.oformat()

  implicit lazy val USER_INPUT_MESSAGE: OFormat[UserInputMessage] = derived.oformat()
}
