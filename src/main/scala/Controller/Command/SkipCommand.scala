package Controller.Command
import Controller.UpdateGameState.updateGameState
import Model.{GameState, User}

class SkipCommand extends Command {

  override def execute(gameState: GameState, currentPlayer: User, playerCardIndex: Int = 0, tableCardIndex: Int = 0): GameState = {
    updateGameState(gameState)
  }
}
