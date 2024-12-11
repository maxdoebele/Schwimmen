package Controller.Command
import Controller.HelpFunctions
import Model._

trait Command {

  def execute() : Unit

  def undoStep(): Unit
  
  def redoStep(): Unit
  
  
}
