package Controller.COR

import Controller.Controller
import Model.BaseImpl.User

trait Handler {

  def handle(controller: Controller, loosers: Seq[User]) : Unit

}
