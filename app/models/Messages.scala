package models

import akka.actor.ActorRef
import models.Tables.{MessageRow, UserRow}

/**
 * actor message entity, only used in server side.
 */
trait Messages

sealed case class EnterMessage(ref: ActorRef, user: UserRow) extends Messages

sealed case class LeaveMessage(user: UserRow) extends Messages

sealed case class UserInputMessage(user: UserRow, message: MessageRow) extends Messages


