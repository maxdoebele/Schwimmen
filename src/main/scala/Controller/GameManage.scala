package Controller

import Model._
import View.TUI

import scala.annotation.tailrec
object GameManage {
  @tailrec
  def playGame(currentGame: GameState): GameState = {
    GameLogic.checkForSchnauz(currentGame)

    if (currentGame.gameOver) {
      println("Das Spiel ist vorbei!")
      currentGame
    } else {
      val currentPlayerIndex = (currentGame.queue - 1) % currentGame.players.size
      val currentPlayer = currentGame.players(currentPlayerIndex)
      
      val newGameStateTUI = new TUI().tuiActionHandler(currentPlayer, currentGame)
      val updatedGameState = newGameStateTUI.copy(queue = newGameStateTUI.queue + 1)

      playGame(updatedGameState)
    }
  }

}
