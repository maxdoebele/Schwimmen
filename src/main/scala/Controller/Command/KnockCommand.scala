package Controller.Command
import Controller.UpdateGameState.updateGameState
import Model._

class KnockCommand extends Command {

  override def execute(gameState: GameState, currentPlayer: User, playerCardIndex: Int = 0, tableCardIndex: Int = 0): GameState = {
    val newKnockCounter = gameState.knockCounter + 1
    updateGameState(gameState, knockCounter = Some(newKnockCounter), gameOver = Some(newKnockCounter >= 2))
  }

}
