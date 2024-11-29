package Controller
import Model._

object skipAction extends GameStrategy {
  override def playerActions(currentPlayer: User, gameState: GameState, playerIndex: Int, tableIndex: Int): GameState = {
    println(s"Spieler ${currentPlayer.name} hat geschoben")
    gameState
  }
}
