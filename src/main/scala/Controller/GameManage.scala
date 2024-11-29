package Controller
import Controller.GameLogic._
import UpdateGameState.updateGameState
import Model.*
import View.TUI

import scala.annotation.tailrec
object GameManage {
  @tailrec
  def playGame(currentGame: GameState): GameState = {
    val checkedSchnauz = GameLogic.checkForSchnauz(currentGame)

    if (checkedSchnauz.gameOver) {
      println("Das Spiel ist vorbei!")
      checkedSchnauz
    } else {
      val currentPlayerIndex = (currentGame.queue - 1) % currentGame.players.size
      val currentPlayer = currentGame.players(currentPlayerIndex)

      val newGameStateTUI = new TUI().tuiActionHandler(currentPlayer, currentGame)

      val gameStateQueuePlusOne = updateGameState(newGameStateTUI, queue = Some(newGameStateTUI.queue + 1))
      playGame(gameStateQueuePlusOne)
    }
  }

  def findLoser(allPlayers: Seq[User]): Seq[User] = {
    val usersPoints: Map[User, Double] = allPlayers.map(user => user -> calculatePoints(user.handDeck)).toMap
    val minPoints = usersPoints.values.min

    allPlayers.filter(user => calculatePoints(user.handDeck) == minPoints)
  }

  def findWinner(allPlayers: Seq[User]): Seq[User] = {
    val usersPoints: Map[User, Double] = allPlayers.map(user => user -> calculatePoints(user.handDeck)).toMap
    val maxPoints = usersPoints.values.max

    allPlayers.filter(user => calculatePoints(user.handDeck) == maxPoints)
  }
  
}