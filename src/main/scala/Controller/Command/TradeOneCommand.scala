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
