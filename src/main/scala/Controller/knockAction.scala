package Controller

import Model._

object knockAction extends GameStrategy {
  override def playerActions(currentPlayer: User, gameState: GameState, playerIndex: Int, tableIndex: Int): GameState = {
    val afterKnockGameState = GameLogic.knock(gameState)
    println(s"Spieler ${currentPlayer.name} hat geklopft")
    afterKnockGameState
  }
}
