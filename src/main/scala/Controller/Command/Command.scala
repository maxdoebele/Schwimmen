package Controller.Command
import Controller.HelpFunctions
import Model.*

trait Command {

  def execute() : Unit

  def undoStep(): Unit
  
  def redoStep(): Unit
  
  
}
