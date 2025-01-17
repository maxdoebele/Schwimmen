package Controller.COR

import Controller.Controller
import Model.BaseImpl.User

/**
 * Trait for Handler 
 * Handles lifePoints and Schwimmer
 * @see CORImpl
 */

trait Handler {

  def handle(controller: Controller, loosers: Seq[User]) : Unit

}
