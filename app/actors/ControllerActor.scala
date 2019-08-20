package actors

import akka.actor.{Actor, ActorLogging, ActorRef}
import com.google.inject.{Inject, Singleton}
import models.{EnterMessage, LeaveMessage, MessageType}
import models.Entity.UserMessage
import models.Formats._
import models.Tables.UserRow
import play.api.libs.json.Json

@Singleton
class ControllerActor @Inject()() extends Actor with ActorLogging {

  var map: Map[Int, (UserRow, ActorRef)] = Map()

  override def receive: Receive = {
    case enter: EnterMessage =>
      // add this man to the map.
      map += (enter.user.id -> (enter.user -> enter.ref))
      // tell other user this man joined in.
      val message = UserMessage(MessageType.USER_ENTER_MESSAGE, System.currentTimeMillis(), enter.user.id, nickname = Some(enter.user.nickname), memberCount = Option(map.size))
      map foreach { data =>
        data._2._2 ! Json.toJson(message).toString()
      }
    case message: UserMessage =>
      map foreach { data =>
        data._2._2 ! Json.toJson(message).toString()
      }
    case leave: LeaveMessage =>
      // remove this man from map
      map -= leave.user.id

      val message = UserMessage(MessageType.USER_LEAVE_MESSAGE, System.currentTimeMillis(), leave.user.id, nickname = Some(leave.user.nickname), memberCount = Option(map.size))

      // tell others
      map foreach { data =>
        data._2._2 ! Json.toJson(message).toString()
      }
  }
}
