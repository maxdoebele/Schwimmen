package Controller.Command
import Model._

trait Command {
  def execute(gameState: GameState, currentPlayer: User, playerCardIndex: Int, tableCardIndex: Int) : GameState
}
