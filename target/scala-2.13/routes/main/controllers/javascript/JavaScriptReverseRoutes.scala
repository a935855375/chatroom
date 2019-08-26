// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/fan/IdeaProject/chatroom/conf/routes
// @DATE:Sat Aug 24 19:59:14 CST 2019

import play.api.routing.JavaScriptReverseRoute


import _root_.controllers.Assets.Asset

// @LINE:3
package controllers.javascript {

  // @LINE:3
  class ReverseHomeController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:9
    def signUp: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.signUp",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "signUp"})
        }
      """
    )
  
    // @LINE:11
    def sendGlobalMessage: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.sendGlobalMessage",
      """
        function(title0,content1) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "notify" + _qS([(""" + implicitly[play.api.mvc.QueryStringBindable[Option[String]]].javascriptUnbind + """)("title", title0), (""" + implicitly[play.api.mvc.QueryStringBindable[String]].javascriptUnbind + """)("content", content1)])})
        }
      """
    )
  
    // @LINE:7
    def socket: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.socket",
      """
        function(token0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "ws" + _qS([(""" + implicitly[play.api.mvc.QueryStringBindable[String]].javascriptUnbind + """)("token", token0)])})
        }
      """
    )
  
    // @LINE:3
    def index: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.index",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + """"})
        }
      """
    )
  
    // @LINE:5
    def login: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.login",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "login"})
        }
      """
    )
  
  }

  // @LINE:14
  class ReverseAssets(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:14
    def versioned: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.Assets.versioned",
      """
        function(file1) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + (""" + implicitly[play.api.mvc.PathBindable[Asset]].javascriptUnbind + """)("file", file1)})
        }
      """
    )
  
  }


}
