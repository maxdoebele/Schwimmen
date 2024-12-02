package Controller.Command

import Controller.UpdateGameState.updateGameState
import Model._

class KnockCommand(initialGameState: GameState, currentPlayer: User) extends Command {

  private var previousState: Option[GameState] = None
  private var newState: Option[GameState] = None

  override def execute(): GameState = {
    previousState = Some(initialGameState)

    val newKnockCounter = initialGameState.knockCounter + 1
    val updatedGameState = updateGameState(initialGameState, knockCounter = Some(newKnockCounter), queue = Some(initialGameState.queue + 1), gameOver = Some(newKnockCounter >= 2)
    )
    
    newState = Some(updatedGameState)
    updatedGameState
  }

  override def undoStep(): Unit = {
    previousState match {
      case Some(state) =>
        newState = Some(initialGameState)
        previousState = None
      case None =>
        throw new IllegalStateException("No previous state to undo to!")
    }
  }

  override def redoStep(): Unit = {
    // Reapply the new state
    newState match {
      case Some(state) =>
        previousState = Some(initialGameState)
        newState = None 
      case None =>
        throw new IllegalStateException("No new state to redo to!")
    }
  }
}
