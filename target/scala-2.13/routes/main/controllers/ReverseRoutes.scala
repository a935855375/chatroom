// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/fan/IdeaProject/chatroom/conf/routes
// @DATE:Tue Aug 20 18:48:18 CST 2019

import play.api.mvc.Call


import _root_.controllers.Assets.Asset

// @LINE:3
package controllers {

  // @LINE:3
  class ReverseHomeController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:9
    def signUp(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "signUp")
    }
  
    // @LINE:7
    def socket(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "ws")
    }
  
    // @LINE:11
    def sendGlobalMessage(title:Option[String], content:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "notify" + play.core.routing.queryString(List(Some(implicitly[play.api.mvc.QueryStringBindable[Option[String]]].unbind("title", title)), Some(implicitly[play.api.mvc.QueryStringBindable[String]].unbind("content", content)))))
    }
  
    // @LINE:3
    def index(): Call = {
      
      Call("GET", _prefix)
    }
  
    // @LINE:5
    def login(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "login")
    }
  
  }

  // @LINE:14
  class ReverseAssets(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:14
    def versioned(file:Asset): Call = {
      implicit lazy val _rrc = new play.core.routing.ReverseRouteContext(Map(("path", "/public"))); _rrc
      Call("GET", _prefix + { _defaultPrefix } + implicitly[play.api.mvc.PathBindable[Asset]].unbind("file", file))
    }
  
  }


}
