package models
import scala.io.Source
import play.api._
import play.api.libs.json._
import play.api.libs.functional.syntax._


case class Account(val name:String, val password:String)

object Account {
	implicit val accoutReader = (
	  (__ \ "name").read[String] and
	  (__ \ "password").read[String]
	)(Account.apply _)

	def default = Account("","")

	def parseJson(file: String, application : DefaultApplication) = {
		val accountConfigFile = application.resourceAsStream(file)
    	val accountJson = accountConfigFile match {
    		case Some(stream) => {
                val jsonString = Source.fromInputStream(stream).mkString
                Json.parse(jsonString)
              }
    		case _ => JsNull
    	}
    	accountJson.validate[Account].asOpt
	}
}