package controllers

import play.api._
import play.api.libs.iteratee._
import play.api.mvc._
import java.io.File
import scala.io.Source
import models._
import play.api.libs.json._

object DigikoApp extends Controller {

  def index = Action {
  	val application = new DefaultApplication(new File("conf/"), this.getClass.getClassLoader, None, Mode.Dev)
  	val accountConfigFile = application.resourceAsStream("account.json")
    val accountJson = accountConfigFile match {
    	case Some(stream) => {
                val jsonString = Source.fromInputStream(stream).mkString
                Json.parse(jsonString)
              }
    	case _ => JsNull
    }
    val account = accountJson.validate[Account].asOpt
    
    val vboxConfigFile = application.resourceAsStream("vbox.json")
    val vboxJson = vboxConfigFile match {
      case Some(stream) => {
                val jsonString = Source.fromInputStream(stream).mkString
                Json.parse(jsonString)
            }
      case _ => JsNull
    }
    val vboxConfig = vboxJson.validate[VBoxConfig].asOpt

    
    val res = VirtualBox(account, vboxConfig).allVms
    val content = views.html.index.render(res)
    Ok(content)
  }
  
  def turnon(name:String) = WebSocket.using[String]{ request => 
    val in = Iteratee.foreach[String](println).mapDone { _ => println("Disconnected")}
    val out = Enumerator("Hello")
    (in, out)
  }
}