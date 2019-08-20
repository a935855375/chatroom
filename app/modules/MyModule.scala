package modules

import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport

import actors._

class MyModule extends AbstractModule with AkkaGuiceSupport {
  override def configure(): Unit = {
    // 将控制actor由guice来管理
    bindActor[ControllerActor]("controller-actor")
    // 绑定ChannelFactory来处理所有的WebSocket请求
    bindActorFactory[ChannelActorFactory, ChannelActorFactory.Factory]
  }
}
