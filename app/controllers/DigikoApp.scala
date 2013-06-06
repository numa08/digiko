package controllers

import play.api._
import play.api.mvc._
import java.io.File
import scala.io.Source
import models._
import play.api.libs.json._
import play.api.libs.functional.syntax._

object DigikoApp extends Controller {

  implicit val vboxReads = (__ \ "port").read[String].map(v => VBox(v))
  
  def index = Action {
  	val application = new DefaultApplication(new File("conf/"), this.getClass.getClassLoader, None, Mode.Dev)
  	val account_config = application.resourceAsStream("account.json")
    val account = account_config match {
    	case Some(stream) => {
                val jsonString = Source.fromInputStream(stream).mkString
                val json = Json.parse(jsonString)
                val nameOpt = json.\("name").asOpt[String]
                val passOpt = json.\("password").asOpt[String]
                for(name <- nameOpt ; pass <- passOpt) yield {
                  Account(name, pass)
                }
              }
    	case _ => None
    }

    val vbox_config = application.resourceAsStream("vbox.json")
    val vbox = vbox_config match {
      case Some(stream) => {
                  val jsonString = Source.fromInputStream(stream).mkString
                  val json = Json.parse(jsonString)
                  val optPort = json.asOpt[VBox]
                }
      case _ => None
    }
    
    val res = account match {
      case Some(account) => account.name + ", " + account.password
      case _ => ""
    }
    Ok(res)
  }
  
}