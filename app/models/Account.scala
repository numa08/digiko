package models
import play.api.libs.json._
import play.api.libs.functional.syntax._


case class Account(val name:String, val password:String)

object Account {
	implicit val accoutReader = (
	  (__ \ "name").read[String] and
	  (__ \ "password").read[String]
	)(Account.apply _)

	def default = Account("","")
}