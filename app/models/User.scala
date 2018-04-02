package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class User(account:String,password:String)
object User {
  implicit val userFormat = Json.format[User]
}
