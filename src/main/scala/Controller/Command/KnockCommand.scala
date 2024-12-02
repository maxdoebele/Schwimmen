package Controller.Command

import Controller.UpdateGameState.updateGameState
import Model._

class KnockCommand(initialGameState: GameState, currentPlayer: User) extends Command {

  var previousState: Option[GameState] = None
  var newState: Option[GameState] = None

  override def execute(): GameState = {
    previousState = Some(initialGameState)

    val newKnockCounter = initialGameState.knockCounter + 1
    val updatedGameState = updateGameState(initialGameState, knockCounter = Some(newKnockCounter), queue = Some(initialGameState.queue + 1), gameOver = Some(newKnockCounter >= 2))

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
