package controllers

import play.api._
import play.api.libs.iteratee._
import play.api.mvc._
import java.io.File
import scala.io.Source
import models._
import play.api.libs.json._

object DigikoApp extends Controller {

  lazy val defaultApplication = new DefaultApplication(new File("conf/"), this.getClass.getClassLoader, None, Mode.Dev)

  def index = Action {
  	val application = defaultApplication
    val account = Account.parseJson("account.json", application)
    val vboxConfig = VBoxConfig.parseJson("vbox.json", application)
    
    val res = VirtualBox(account, vboxConfig).allVms
    val content = views.html.index.render(res)
    Ok(content)
  }
  
  def turnon(name:String) = Action{ request => 
    
    val application = defaultApplication
    val account = Account.parseJson("account.json", application)
    val vboxConfig = VBoxConfig.parseJson("vbox.json", application)
    val vbox = VirtualBox(account, vboxConfig)

    val machine = vbox.findVm(name)
    machine match {
      case Some(machine) => {
                              vbox.turnOn(machine.getName)
                              Ok(machine.getState.name)
                            }
      case _ => Ok("Not found machine")
    }
  }

  def status(name: String) = WebSocket.using[String] { request =>
    val in = Iteratee.foreach[String](_ => Unit)
    val out = Enumerator(name)

    (in, out)
  }
}