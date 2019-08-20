package models

import akka.actor.ActorRef
import models.Entity.UserMessage
import models.Tables.UserRow

/**
 * actor message entity, only used in server side.
 */
trait Messages

sealed case class EnterMessage(ref: ActorRef, user: UserRow) extends Messages

sealed case class LeaveMessage(user: UserRow) extends Messages

sealed case class UserInputMessage(user: UserRow, message: UserMessage) extends Messages


