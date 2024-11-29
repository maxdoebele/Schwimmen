package Controller
import Model._
trait GameStrategy {
  def playerActions(currentPlayer: User, gameState: GameState, playerIndex: Int, tableIndex: Int): GameState
}
