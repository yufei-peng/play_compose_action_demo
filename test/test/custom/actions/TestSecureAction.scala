package test.custom.actions

import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.{FakeRequest, PlaySpecification}
import play.api.mvc.Results

class TestSecureAction extends PlaySpecification with Results {

  val appliccation:Application = GuiceApplicationBuilder().build()

  val testCustomController = appliccation.injector.instanceOf[controllers.TestCustomController]

  "Test Secure Action" should {

    "requst has the right header and the content is correct" in {

      val correctRequest = FakeRequest("POST", "/test").withHeaders(("x-asclepius", "pass"))
      val result = testCustomController.testTestSercureAction(correctRequest)

      status(result) mustEqual 200
      contentAsString(result) mustEqual "test OK"

    }

    "request has the right header but with wrong content" in {

      val correctRequest = FakeRequest("POST", "/test").withHeaders(("x-asclepius", "xxx"))
      val result = testCustomController.testTestSercureAction(correctRequest)

      status(result) mustEqual 403
    }

    "request has the wrong header and the wrong content" in {

      val correctRequest = FakeRequest("POST", "/test")
      val result = testCustomController.testTestSercureAction(correctRequest)

      status(result) mustEqual 400
    }

  }

}
