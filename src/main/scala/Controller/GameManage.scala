package Controller
import Controller.GameBuilder._
import Controller.HelpFunctions._
import UpdateGameState.updateGameState
import Model._
import View.TUI

import scala.annotation.tailrec
object GameManage {

  val tui = new TUI()
  
  def playGame(currentGame: GameState): GameState = {
    
    if (currentGame.players.size <= 1) {
      currentGame
    } else {
      tui.displayGameState(currentGame)
      val currentRound = GameManage.playRound(currentGame)
      val finishedRound = UpdateGameState.updateLivePoints(currentRound, GameManage.findLoserOfRound(currentRound.players))
      val newRound = BuildNewRound(finishedRound).returnGameState()

      playGame(newRound) // Recursively call the function with the new game state
    }
  }


  @tailrec
  def playRound(currentGame: GameState): GameState = {
    val checkedSchnauz = HelpFunctions.checkForSchnauz(currentGame)

    if (checkedSchnauz.gameOver) {
      println("Die Runde ist vorbei!")
      checkedSchnauz
    } else {
      val currentPlayer = HelpFunctions.getCurrentPlayer(currentGame)

      val newGameStateTUI = tui.tuiActionHandler(currentPlayer, currentGame)
      
      playRound(newGameStateTUI)
    }
  }
  

  def findLoserOfRound(allPlayers: Seq[User]): Seq[User] = {
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