package controllers

import actors.ChannelActorFactory
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.stream.Materializer
import com.google.inject._
import com.google.inject.name.Named
import models.Entity.{JWTToken, LoginResponse, UserRequest}
import models.Formats._
import models.MessageType
import models.Tables._
import models.Tables.profile.api._
import play.api.Logger
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.InjectedActorSupport
import play.api.libs.json.{JsValue, Json}
import play.api.libs.streams.ActorFlow
import play.api.mvc._
import play.db.NamedDatabase
import service.{AuthService, UserService}
import slick.jdbc.JdbcProfile
import utils.ErrorFactory

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HomeController @Inject()(
                                cc: ControllerComponents,
                                actorFactory: ChannelActorFactory.Factory,
                                errorHandler: ErrorFactory,
                                authService: AuthService,
                                userService: UserService,
                                assets: Assets,
                                @NamedDatabase("server")
                                protected val dbConfigProvider: DatabaseConfigProvider,
                                @Named("controller-actor")
                                controllerActor: ActorRef
                              )
                              (
                                implicit
                                ec: ExecutionContext,
                                system: ActorSystem,
                                mat: Materializer
                              )
  extends AbstractController(cc) with InjectedActorSupport with HasDatabaseConfigProvider[JdbcProfile] {

  lazy val logger = Logger(this.getClass)

  def index: Action[AnyContent] = this.assets.at("index.html")

  def login: Action[JsValue] = Action.async(parse.json) { request =>
    request.body.asOpt[UserRow] match {
      case Some(input) =>
        db.run(User.filter(x => x.email === input.email && x.password === input.password).result.headOption).map {
          case Some(res) =>
            val timeOut = System.currentTimeMillis() + 3600 * 1000
            val token = authService.createToken(Json.toJson(JWTToken(res.id, res.email, timeOut)).toString())
            // 用户登录成功了就直接给用户返回token
            Ok(LoginResponse(timeOut, token, res.id, res.nickname))
          case None =>
            Unauthorized(errorHandler.generateCommonError("Email or password is incorrect."))
        }
      case None =>
        Future.successful(BadRequest(errorHandler.InvalidParam))
    }
  }

  def signUp: Action[JsValue] = Action.async(parse.json) { request =>
    // 先把用户传过来的json转化为UserRow
    request.body.asOpt[UserRow] match {
      case Some(input) =>
        // 去数据库查询是否有相同的用户名和昵称
        db.run(User.filter(x => x.email === input.email || x.nickname === input.nickname).result.headOption).flatMap {
          case Some(_) =>
            // 生成409冲突响应
            Future.successful(Conflict(errorHandler.generateCommonError("The same email address or nickname already exists.")))
          case None =>
            db.run(User += input).map {
              // 返回一条记录说明插入成功
              case 1 => Ok(errorHandler.generateSuccessInfo("Sign up successfully."))
              // 其他的情况都视为异常
              case _ => InternalServerError(errorHandler.ServerInternalError)
            }
        }
      case None =>
        // 转换失败的情况
        Future.successful(BadRequest(errorHandler.InvalidParam))
    }
  }

  def sendGlobalMessage(title: Option[String], content: String): Action[AnyContent] = loginAuth {
    Action { _ =>
      logger.info(s"raised a global message with title : `$title` : $content")
      val message = MessageRow(0, MessageType.GLOBAL_MESSAGE, System.currentTimeMillis(), -1, title = title, content = Some(content))
      controllerActor ! message
      Ok("send successfully!")
    }
  }

  // 将请求交给ChannelActor来处理
  def socket(token: String): WebSocket = WebSocket.acceptOrResult[String, String] { _ =>
    if (authService.isValidToken(token)) {
      val uid = Json.parse(authService.decodePayload(token).get).\("uid").as[Int]
      userService.getUserByUid(uid).map { user =>
        Right(ActorFlow.actorRef { out =>
          Props(actorFactory(user, out))
        })
      }
    } else
      Future.successful(Left(Unauthorized("Invalid credential")))
  }


  def loginAuth[A](action: Action[A]): Action[A] = Action.async(action.parser) { request =>
    request.headers.get("Authorization") match {
      case Some(token) =>
        if (authService.isValidToken(token))
          action(request)
        else
          Future.successful(Unauthorized("Invalid credential"))
      case None =>
        Future.successful(Forbidden("Only Authorized requests allowed"))
    }
  }


  def parseUser(implicit ec: ExecutionContext): ActionRefiner[Request, UserRequest] = new ActionRefiner[Request, UserRequest] {
    override protected def refine[A](request: Request[A]): Future[Either[Result, UserRequest[A]]] = {
      request.headers.get("Authorization") match {
        case Some(token) =>
          if (authService.isValidToken(token)) {
            val uid = Json.parse(authService.decodePayload(token).get).\("uid").as[Int]
            userService.getUserByUid(uid).map(user => Right(UserRequest(user, request)))
          } else
            Future.successful(Left(Unauthorized("Invalid credential")))
        case None =>
          Future.successful(Left(Forbidden("Only Authorized requests allowed")))
      }
    }

    override protected def executionContext: ExecutionContext = ec
  }
}
