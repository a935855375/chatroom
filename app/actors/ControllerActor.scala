package actors

import akka.actor.{Actor, ActorLogging, ActorRef}
import com.google.inject.{Inject, Singleton}
import models.{EnterMessage, LeaveMessage, MessageType}
import models.Formats._
import models.Tables.{MessageRow, UserRow}
import play.api.libs.json.Json
import service.MessageService

import scala.concurrent.ExecutionContext

@Singleton
class ControllerActor @Inject()(
                                 messageService: MessageService
                               )
                               (
                                 implicit
                                 ec: ExecutionContext
                               )
  extends Actor with ActorLogging {

  var map: Map[Int, (UserRow, ActorRef)] = Map()

  override def receive: Receive = {
    case enter: EnterMessage =>
      // add this man to the map.
      map += (enter.user.id -> (enter.user -> enter.ref))
      // tell other user this man joined in.
      val message = MessageRow(0, MessageType.USER_ENTER_MESSAGE, System.currentTimeMillis(), enter.user.id, nickname = Some(enter.user.nickname), memberCount = Option(map.size))
      map foreach { data =>
        data._2._2 ! Json.toJson(message).toString()
      }

      // push history message to this one
      this.messageService.getHistoryMessage().map { messages =>
        enter.ref ! Json.toJson(MessageRow(0, MessageType.HISTORY_MESSAGES, System.currentTimeMillis(), enter.user.id, content = Some(Json.toJson(messages).toString()))).toString()
      }

    case message: MessageRow =>
      map foreach { data =>
        data._2._2 ! Json.toJson(message).toString()
      }

      this.messageService.save(message)

    case leave: LeaveMessage =>
      // remove this man from map
      map -= leave.user.id

      val message = MessageRow(0, MessageType.USER_LEAVE_MESSAGE, System.currentTimeMillis(), leave.user.id, nickname = Some(leave.user.nickname), memberCount = Option(map.size))

      // tell others
      map foreach { data =>
        data._2._2 ! Json.toJson(message).toString()
      }
  }
}
