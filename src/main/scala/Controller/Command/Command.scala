package Controller.Command
import Model.*

trait Command {

  def execute() : GameState

  def undoStep(): Option[GameState]
  
  def redoStep(): Option[GameState]
  
}
