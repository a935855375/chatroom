// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/fan/IdeaProject/chatroom/conf/routes
// @DATE:Sat Aug 24 19:59:14 CST 2019


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
