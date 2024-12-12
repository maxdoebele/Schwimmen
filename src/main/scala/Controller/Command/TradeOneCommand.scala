package Controller.Command
import Controller.util.Controller
import _root_.Controller.HelpFunctions
import Model._

class TradeOneCommand(controller: Controller) extends Command {

  var memento: GameState = controller.gameState

  override def execute(): Unit = {
    memento = controller.gameState
    val currentPlayer = HelpFunctions.getCurrentPlayer(controller.gameState)
    val Some((userWithTwoCards, playerCard)) = currentPlayer.removeCard(controller.gameState.indexCardPlayer)
    val Some(tableWithTwoCards, tableCard) = controller.gameState.table.removeCard(controller.gameState.indexCardTable)

    val userWithNewCards = userWithTwoCards.addCard(tableCard)
    val tableWithThreeCards = tableWithTwoCards.addCard(playerCard)

    val updatedPlayers = controller.gameState.players.map { player =>
      if (currentPlayer.name == player.name) userWithNewCards else player
    }
    
    val updatedGameState = controller.gameState.copy(players = updatedPlayers, table = tableWithThreeCards, queue = controller.gameState.queue + 1)
    controller.gameState = updatedGameState
  }

  override def undoStep(): Unit = {
    val memento = controller.gameState
    controller.gameState = this.memento
    this.memento = memento
  }

  override def redoStep(): Unit = {
    val memento = controller.gameState
    controller.gameState = this.memento
    this.memento = memento
  }

}
