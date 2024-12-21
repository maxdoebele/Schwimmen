package Controller.Command.CommandImpl

import Model.BaseImpl.User
import Model.GameStateTrait
import _root_.Controller.Command.Command
import _root_.Controller.{Controller, HelpFunctions}

class TradeAllCommand(controller: Controller) extends Command {
  var memento: GameStateTrait = controller.gameState

  override def execute(): Unit = {
    memento = controller.gameState
    
    val currentPlayer = HelpFunctions.getCurrentPlayer(controller.gameState)
    val (userWithNoCards, playerCards) = currentPlayer.removeAllCards()
    val (tableWithNoCards, tableCards) = controller.gameState.table.removeAllCards()
    val userWithNewCards = userWithNoCards.add3Cards(tableCards)
    val tableWithNewCards = tableWithNoCards.add3Cards(playerCards)

    val updatedPlayers = controller.gameState.players.map { player =>
      if (currentPlayer.name == player.name) userWithNewCards else player
    }

    val updatedGameState = controller.gameState.copy(players = updatedPlayers, table = tableWithNewCards, queue = controller.gameState.queue + 1)
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
