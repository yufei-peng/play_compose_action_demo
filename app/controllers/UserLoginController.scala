package controllers

import javax.inject._
import play.api.mvc._

import models.User


// jwt
import pdi.jwt.JwtSession
import play.api.libs.json.Json
import play.api.libs.functional.syntax._

import pdi.jwt._
import pdi.jwt.JwtSession._

class UserLoginController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {


  private val logger = play.api.Logger(this.getClass)

  /**
    * 用戶Login時，接受request之body，轉換成JWT
    *
    *   用戶傳入帳號密碼，我方使用play secret key 製作signature
    *
    * */
  def loginAction = Action(parse.json){ implicit request =>

    implicit val userJsonFormat = Json.format[User]


    val userJsResult = Json.fromJson(request.body)(userJsonFormat)


    userJsResult.asOpt match {
      case Some(user) => {
        val jwtSession = JwtSession().+("user",user)
        val result = Ok("This response has integrate jwt").withJwtSession(jwtSession)
//        println(jwtSession.serialize)
//        println(JwtSession.deserialize(jwtSession.serialize))
//        println(result)
        result
      }
      case None =>  BadRequest
    }
  }


}
