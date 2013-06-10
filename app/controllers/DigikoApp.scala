package controllers

import play.api._
import play.api.mvc._
import java.io.File
import scala.io.Source
import models._
import play.api.libs.json._
import play.api.libs.functional.syntax._

object DigikoApp extends Controller {

  implicit val accoutReader = (
      (__ \ "name").read[String] and
      (__ \ "password").read[String]
    )(Account.apply _)

  implicit val vboxReader = (
      (__ \ "port").read[String] and
      (__ \ "host").read[String]
    )(VBox.apply _)

  def index = Action {
  	val application = new DefaultApplication(new File("conf/"), this.getClass.getClassLoader, None, Mode.Dev)
  	val accountConfig = application.resourceAsStream("account.json")
    val accountJson = accountConfig match {
    	case Some(stream) => {
                val jsonString = Source.fromInputStream(stream).mkString
                Json.parse(jsonString)
              }
    	case _ => JsNull
    }
    val account = accountJson.validate[Account].asOpt
    
    val vboxConfig = application.resourceAsStream("vbox.json")
    val vboxJson = vboxConfig match {
      case Some(stream) => {
                val jsonString = Source.fromInputStream(stream).mkString
                Json.parse(jsonString)
            }
      case _ => JsNull
    }
    val vbox = vboxJson.validate[VBox].asOpt
    
    Ok(account + "" + vbox)
  }
  
}