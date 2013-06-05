package controllers

import play.api._
import play.api.mvc._
import java.io.File
import scala.io.Source

object DigikoApp extends Controller {
  
  def index = Action {
  	val application = new DefaultApplication(new File("conf/"), this.getClass.getClassLoader, None, Mode.Dev)
  	val conf = application.resourceAsStream("account.json")
    // Ok(views.html.index("Your new application is ready."))
    val res = conf match {
    	case Some(stream) => Source.fromInputStream(stream).mkString
    	case _ => "No"
    }
    Ok(res)
  }
  
}