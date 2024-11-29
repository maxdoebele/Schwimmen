package Controller
import Model._

object tradeAllAction extends GameStrategy {
  override def playerActions(currentPlayer: User, gameState: GameState, playerIndex: Int, tableIndex: Int): GameState = {
    val (userWithNoCards, playerCards) = currentPlayer.removeAllCards()
    val (tableWithNoCards, tableCards) = gameState.table.removeAllCards()
    val userWithNewCards = userWithNoCards.add3Cards(tableCards)
    val tableWithNewCards = tableWithNoCards.add3Cards(playerCards)
    val updatedPlayers = gameState.players.map { player =>
      if (player.name == currentPlayer.name) userWithNewCards else player
    }
    gameState.copy(players = updatedPlayers, table = tableWithNewCards)
  }
}
