package Controller.Command
import Controller.UpdateGameState.updateGameState
import Model.{GameState, User}

class TradeAllCommand extends Command {
  override def execute(gameState: GameState, currentPlayer: User, playerCardIndex: Int = 0, tableCardIndex: Int = 0): GameState = {
    val (userWithNoCards, playerCards) = currentPlayer.removeAllCards()
    val (tableWithNoCards, tableCards) = gameState.table.removeAllCards()
    val userWithNewCards = userWithNoCards.add3Cards(tableCards)
    val tableWithNewCards = tableWithNoCards.add3Cards(playerCards)
    updateGameState(gameState, Some(currentPlayer), Some(userWithNewCards), Some(tableWithNewCards))
  }
}
