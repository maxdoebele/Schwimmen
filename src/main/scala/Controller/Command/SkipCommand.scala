package Controller.Command
import Controller.UpdateGameState.updateGameState
import Model.{GameState, User}

class SkipCommand(initialGameState: GameState) extends Command {
  private var previousState: Option[GameState] = None
  private var newState: Option[GameState] = None
  
  override def execute(): GameState = {
    previousState = Some(initialGameState)
    val updatedGameState = updateGameState(initialGameState,queue = Some(initialGameState.queue + 1))
    newState = Some(updatedGameState)
    updatedGameState
  }

  override def undoStep(): Option[GameState] = {
    previousState match {
      case Some(state) =>
        newState = previousState
        previousState = newState
        newState
      case None =>
        None
    }
  }

  override def redoStep(): Option[GameState] = {
    newState match {
      case Some(state) =>
        previousState = newState
        newState = previousState
        newState
      case None =>
        None
    }
  }
}
