package models
import play.api.libs.json._
import play.api.libs.functional.syntax._


case class VBoxConfig(val port:String, val host:String)

object VBoxConfig {
	implicit val vboxReader = (
	  (__ \ "port").read[String] and
	  (__ \ "host").read[String]
	)(VBoxConfig.apply _)

	def default = VBoxConfig("10080", "localhost")
}