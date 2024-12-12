package Controller.COR

import Controller.util.Controller
import Model.User

trait Handler {

  def handle(controller: Controller, loosers: Seq[User]) : Unit

}
