package models

case class Account(val name:String, val password:String)

object Account {
	def default = Account("","")
}