package unit.controllers

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.{EssentialAction, Results}
import play.api.test.{FakeRequest, Helpers, PlaySpecification}
import play.api.libs.json.Json



import pdi.jwt._
import pdi.jwt.JwtSession._

// https://github.com/playframework/playframework/issues/7877

class TestUserLoginController extends PlaySpecification  with Results {

  val application: Application = GuiceApplicationBuilder().build()
  val userLoginController = application.injector.instanceOf[controllers.UserLoginController]

//  目前抓不出，不知為何需要一個ActorSystem
  implicit val sys = ActorSystem("MyTest")
  implicit val mat = ActorMaterializer()

  "Custom Secure Action" should {

    "response has valid header " in {

      val correctRequest = FakeRequest(POST, "/").withBody(
        Json.parse("""{"password":"str","account":"dd5"}""")
      ).withHeaders(("x-example","14"),("Content-type","application/json"))

      val result = userLoginController.loginAction.apply(correctRequest)

      status(result) mustEqual 200
      println(headers(result).get("Authorization").get)
      println(JwtSession.deserialize(headers(result).get("Authorization").get))
      println(JwtSession.deserialize(headers(result).get("Authorization").get).headerData)
      println(JwtSession.deserialize(headers(result).get("Authorization").get).claimData)
      contentAsString(result) mustEqual "Header test has passed"

    }

  }

}
