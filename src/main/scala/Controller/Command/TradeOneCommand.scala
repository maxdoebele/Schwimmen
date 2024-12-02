package Controller.Command
import Controller.UpdateGameState.updateGameState
import Model._

class TradeOneCommand(initialGameState: GameState, currentPlayer: User, playerCardIndex: Int, tableCardIndex: Int) extends Command {
  private var previousState: Option[GameState] = None
  private var newState: Option[GameState] = None
  
  override def execute(): GameState = {
    previousState = Some(initialGameState)
    val Some((userWithTwoCards, playerCard)) = currentPlayer.removeCard(playerCardIndex)
    val Some(tableWithTwoCards, tableCard) = initialGameState.table.removeCard(tableCardIndex)
    
    val userWithThreeCards = userWithTwoCards.addCard(tableCard)
    val tableWithThreeCards = tableWithTwoCards.addCard(playerCard)
    
    val updatedGameState = updateGameState(initialGameState, Some(currentPlayer), Some(userWithThreeCards), Some(tableWithThreeCards), queue = Some(initialGameState.queue + 1))
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
