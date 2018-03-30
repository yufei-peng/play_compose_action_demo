package test.custom.actions

import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.{EssentialAction, Results}
import play.api.test.{FakeRequest, Helpers, PlaySpecification, WithApplication}

class TestCustomSecureAction extends PlaySpecification  with Results {

  val application: Application = GuiceApplicationBuilder().build()
  val customSecureActionController = application.injector.instanceOf[controllers.TestCustomController]

  "Custom Secure Action" should {

    "request has valid header and value is correct" in {

      val correctRequest = FakeRequest(POST, "/").withHeaders(("x-example","14"))
      val result = customSecureActionController.testCustomSecureAction(correctRequest)

      status(result) mustEqual 200
      contentAsString(result) mustEqual "Header test has passed"

    }

    "request has valid header and value is incorrect" in {

      val correctRequest = FakeRequest(POST, "/").withHeaders(("x-example","it's a string"))
      val result = customSecureActionController.testCustomSecureAction(correctRequest)
      status(result) mustEqual 403

    }

    "request has invalid header" in {

      val correctRequest = FakeRequest(POST, "/")
      val result = customSecureActionController.testCustomSecureAction(correctRequest)
      status(result) mustEqual 400

    }
  }
}
