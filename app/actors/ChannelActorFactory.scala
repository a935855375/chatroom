package actors

import akka.actor.{Actor, ActorLogging, ActorRef}
import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import com.google.inject.name.Named
import models.Entity.UserMessage
import models.Formats._
import models.{EnterMessage, LeaveMessage, MessageType, Tables}
import play.api.libs.json.Json
import service.UserService

object ChannelActorFactory {

  trait Factory {
    def apply(out: ActorRef): Actor
  }

}

class ChannelActorFactory @Inject()(@Assisted out: ActorRef,
                                    @Named("controller-actor") controllerActor: ActorRef,
                                    userService: UserService) extends Actor with ActorLogging {

  import context.dispatcher

  var referenceUser: Option[Tables.UserRow] = None

  override def receive: Receive = {
    case msgString: String =>

      val userMessage = Json.parse(msgString).as[UserMessage]

      log.debug(s"A message with type ${userMessage.`type`} from ${userMessage.uid} received: `${userMessage.content}`")

      userMessage.`type` match {
        case MessageType.HEART_BEAT =>
          out ! Json.toJson(userMessage.copy(time = System.currentTimeMillis())).toString()

        case MessageType.USER_INPUT_MESSAGE =>
          userService.getUserByUid(userMessage.uid) foreach { user =>
            this.referenceUser = Some(user)
            controllerActor ! userMessage.copy(nickname = Some(user.nickname))
          }

        case MessageType.USER_ENTER_MESSAGE =>
          userService.getUserByUid(userMessage.uid) foreach { user =>
            this.referenceUser = Some(user)
            controllerActor ! EnterMessage(out, user)
            log.info(s"${user.nickname} joined the chat room.")
          }
      }

    case _ =>
  }

  override def postStop(): Unit = {
    log.info(s"${this.referenceUser.map(_.nickname).getOrElse("Some body who did not enter")} left the chat room.")
    this.controllerActor ! LeaveMessage(referenceUser.get)
  }
}
