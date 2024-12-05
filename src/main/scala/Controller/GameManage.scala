package Controller
import Controller.GameBuilder.*
import Controller.HelpFunctions.*
import Controller.util.Controller
import Model.*
import View.TUI

import scala.annotation.tailrec
object GameManage {


  def playGame(controller: Controller): Controller = {
    val tui = new TUI(controller)
    if (controller.gameState.players.size <= 1) {
      return controller
    }
    tui.update()

    val newRound = BuildNewRound(controller.gameState).returnController()

    playGame(newRound) // Recursively call the function with the new game state
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