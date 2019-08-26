package actors

import akka.actor.{Actor, ActorLogging, ActorRef}
import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import com.google.inject.name.Named
import models.Formats._
import models.Tables.{MessageRow, UserRow}
import models.{EnterMessage, LeaveMessage, MessageType, Tables}
import play.api.libs.json.Json
import service.UserService

object ChannelActorFactory {

  trait Factory {
    def apply(user: UserRow, out: ActorRef): Actor
  }

}

class ChannelActorFactory @Inject()(@Assisted user: UserRow,
                                    @Assisted out: ActorRef,
                                    @Named("controller-actor") controllerActor: ActorRef,
                                    userService: UserService) extends Actor with ActorLogging {

  import context.dispatcher

  override def receive: Receive = {
    case msgString: String =>
      val userMessage = Json.parse(msgString).as[MessageRow]

      log.debug(s"A message with type ${userMessage.`type`} from ${userMessage.uid} received: `${userMessage.content}`")

      userMessage.`type` match {
        case MessageType.HEART_BEAT =>
          out ! Json.toJson(userMessage.copy(time = System.currentTimeMillis())).toString()

        case MessageType.USER_INPUT_MESSAGE =>
          userService.getUserByUid(userMessage.uid) foreach { user =>
            controllerActor ! userMessage.copy(nickname = Some(user.nickname))
          }

        case MessageType.USER_ENTER_MESSAGE =>
          userService.getUserByUid(userMessage.uid) foreach { user =>
            controllerActor ! EnterMessage(out, user)
            log.info(s"${user.nickname} joined the chat room.")
          }
      }

    case _ =>
  }

  override def postStop(): Unit = {
    log.info(s"${this.user.nickname} left the chat room.")
    this.controllerActor ! LeaveMessage(this.user)
  }
}
