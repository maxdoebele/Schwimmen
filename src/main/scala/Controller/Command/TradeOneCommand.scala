package Controller.Command
import Controller.UpdateGameState.updateGameState
import Model._

class TradeOneCommand extends Command {

  override def execute(gameState: GameState, currentPlayer: User, playerCardIndex: Int, tableCardIndex: Int): GameState = {
    val Some((userWithTwoCards, playerCard)) = currentPlayer.removeCard(playerCardIndex)
    val Some((tableWithTwoCards, tableCard)) = gameState.table.removeCard(tableCardIndex)
    val userWithThreeCards = userWithTwoCards.addCard(tableCard)
    val tableWithThreeCards = tableWithTwoCards.addCard(playerCard)
    updateGameState(gameState, Some(currentPlayer), Some(userWithThreeCards), Some(tableWithThreeCards))
  }

}
