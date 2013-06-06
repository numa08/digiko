package controllers

import play.api._
import play.api.mvc._
import java.io.File
import scala.io.Source
import models._
import play.api.libs.json.Json

object DigikoApp extends Controller {
  
  def index = Action {
  	val application = new DefaultApplication(new File("conf/"), this.getClass.getClassLoader, None, Mode.Dev)
  	val conf = application.resourceAsStream("account.json")
    // Ok(views.html.index("Your new application is ready."))
    val config = conf match {
    	case Some(stream) => Source.fromInputStream(stream).mkString
    	case _ => "No"
    }
    val json = Json.parse(config
      )
    val name = json.\("name").asOpt[String]
    val password = json.\("password").asOpt[String]

    val account = for(acName <- name; acPassword <- password) yield{
      Account(acName, acPassword)
    }
    val res = account match {
      case Some(account) => account.name + ", " + account.password
      case _ => ""
    }
    Ok(res)
  }
  
}