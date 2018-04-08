package custom.actions

import javax.inject.Inject

import play.api.mvc.{ActionBuilderImpl, BodyParsers, Request, Result}
import play.api.mvc.Results.{BadRequest,Forbidden}

import scala.concurrent.{ExecutionContext, Future}

class TestSecureAction @Inject()(parser:BodyParsers.Default)(implicit ec: ExecutionContext) extends ActionBuilderImpl(parser){

  override def invokeBlock[A](request:Request[A], block: (Request[A]) => Future[Result]) = {

    request.headers.toSimpleMap.get("x-asclepius").fold(
      ifEmpty = Future(BadRequest("Http request Content not valid"))
    ){ successFindValue =>

      successFindValue.compareToIgnoreCase("pass") match {
        case 0 => {
          block(request)
        }
        case _ => {
          Future(Forbidden("The Value of x-asclepius not valid"))
        }
      }

    }

  }

}
