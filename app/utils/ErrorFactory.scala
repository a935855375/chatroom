package utils

import com.google.inject.{Inject, Singleton}

object ErrorFactory {

  case class Error(errType: String, msg: String)

}

@Singleton
class ErrorFactory @Inject()() {

  import ErrorFactory._

  lazy val InvalidParam: Error = generateCommonError("Invalid Parameter")

  lazy val ServerInternalError: Error = generateServerError("Server Internal Error")

  def generateSuccessInfo(msg: String) = Error("Success", msg)

  def generateCommonError(msg: String) = Error("CommonError", msg)

  def generateServerError(msg: String) = Error("ServerInternalError", msg)
}
