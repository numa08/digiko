package models

import org.virtualbox_4_2.VirtualBoxManager
import org.virtualbox_4_2.IMachine
import scala.collection.JavaConversions._

case class VirtualBox(val account:Account, val vbox:VBoxConfig){
	lazy val vboxManaager = () => {
		try { 
			val manager = VirtualBoxManager.createInstance(null)
			manager.connect("http://" + vbox.host + ":" + vbox.port, account.name, account.password)
			Option(manager)
		} catch {
	     	case e: Exception => None
		}
	}
	def allVms = {
		vboxManaager() match {
			case Some(manager) => manager.getVBox().getMachines().toList
			case _ => Nil
		}
	}
}

object VirtualBox {
	def apply(account:Option[Account], vbox:Option[VBoxConfig]) = {
		(account, vbox) match {
			case (Some(ac), Some(vb)) => new VirtualBox(ac,vb)
			case _ => new VirtualBox(Account.default, VBoxConfig.default)
			}
	}
}