package Controller.Command
import Model.*

trait Command {

  def execute() : GameState

  def undoStep(): Unit
  
  def redoStep(): Unit
  
}
