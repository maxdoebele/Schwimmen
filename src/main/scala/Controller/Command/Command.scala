package Controller.Command
import Model.*

trait Command {

  def execute() : Unit

  def undoStep(): Unit
  
  def redoStep(): Unit
  
}
