package models
import scala.io.Source
import play.api._
import play.api.libs.json._
import play.api.libs.functional.syntax._


case class VBoxConfig(val port:String, val host:String)

object VBoxConfig {
	implicit val vboxReader = (
	  (__ \ "port").read[String] and
	  (__ \ "host").read[String]
	)(VBoxConfig.apply _)

	def default = VBoxConfig("10080", "localhost")

	def parseJson(file: String, application : DefaultApplication) = {
		val vboxConfigFile = application.resourceAsStream(file)
    	val vboxJson = vboxConfigFile match {
  			case Some(stream) => {
            	val jsonString = Source.fromInputStream(stream).mkString
            	Json.parse(jsonString)
        		}
  			case _ => JsNull
    	}
	    vboxJson.validate[VBoxConfig].asOpt
	}
}