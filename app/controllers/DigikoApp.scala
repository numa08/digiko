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
    val account = Account.parseJson("account.json", application)
    val vboxConfig = VBoxConfig.parseJson("vbox.json", application)
    
    val res = VirtualBox(account, vboxConfig).allVms
    val content = views.html.index.render(res)
    Ok(content)
  }
  
  def turnon(name:String) = WebSocket.using[String]{ request => 
    val in = Iteratee.foreach[String](println).mapDone { _ => println("Disconnected")}
    val out = Enumerator(name)
    (in, out)
  }
}