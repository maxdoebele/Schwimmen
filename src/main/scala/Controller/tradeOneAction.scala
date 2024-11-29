package Controller
import Model._

object tradeOneAction extends GameStrategy {
  override def playerActions(currentPlayer: User, gameState: GameState, playerIndex: Int, tableIndex: Int): GameState = {
    val Some((userWithTwoCards, playerCard)) = currentPlayer.removeCard(playerIndex)
    val Some((tableWithTwoCards, tableCard)) = gameState.table.removeCard(tableIndex)
    val userWithThreeCards = userWithTwoCards.addCard(tableCard)
    val tableWithThreeCards = tableWithTwoCards.addCard(playerCard)
    val updatedPlayers = gameState.players.map { player =>
      if (player.name == currentPlayer.name) userWithThreeCards else player
    }
    gameState.copy(players = updatedPlayers, table = tableWithThreeCards)
  }
}
