package models

import org.virtualbox_4_2.VirtualBoxManager
import org.virtualbox_4_2.IMachine
import scala.collection.JavaConversions._

case class VirtualBox(val account:Account, val vbox:VBox){
	lazy val vboxManaager = () => {
		val manager = VirtualBoxManager.createInstance(null)
		manager.connect("http://" + vbox.host + ":" + vbox.port, account.name, account.password)
		manager
	}
	def allVms = {
		vboxManaager().getVBox().getMachines().toList
	}
}

object VirtualBox {
	def apply(account:Option[Account], vbox:Option[VBox]) = {
		(account, vbox) match {
			case (Some(ac), Some(vb)) => new VirtualBox(ac,vb)
			case _ => new VirtualBox(Account.default, VBox.default)
			}
	}
}