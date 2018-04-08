package custom.actions

import play.api.mvc._

import javax.inject.Inject
import play.api.http.HttpEntity
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import play.api.mvc.Results.{BadRequest,Forbidden}

import scala.util.{Try,Success,Failure}

// parser: 封包解析器
// ec : 高並發線程資源池
class CustomSecureAction @Inject()(parser: BodyParsers.Default)(implicit ec: ExecutionContext) extends ActionBuilderImpl(parser){

  // logger
  private val logger = play.api.Logger(this.getClass)


  // 當封包進來Action 的時候，提前做的處理。
  // [A] 泛型：讓此方法可接受的物件，秉鴻找相應文件給大家看。
  override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {

    /**
      *
      * 取出Header特定欄位，並檢驗。
      *
      * * 檢驗Header是否有x-example欄位
      * *      若有
      * *        檢驗x-example欄位是否能轉為數字
      * *          若能轉為數字
      * *            則放行
      * *          若不能轉為數字
      * *            傳回Forbidden
      * *      若無
      * *        傳回BadRequest
      *
      * */
    request.headers.toSimpleMap.get("x-example").fold(
      ifEmpty =  Future(BadRequest("u can write something for bad request"))
    ){ headerValue =>
      Try{headerValue.toInt} match{
        case Success(e) => {
          block(request)
        }
        case _ => {
          Future(Forbidden("u can write something for forbidden"))
        }
      }
    }
  }

}
