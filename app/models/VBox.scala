package models

case class VBox(val port:String, val host:String)

object VBox {
	def default = VBox("10080", "localhost")
}