package Controller
import Controller.GameBuilder._
import Controller.HelpFunctions._
import Controller.util.Controller
import Model._
import View.TUI

import scala.annotation.tailrec
import scala.util._
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
    val usersPoints: Seq[(User, Try[Double])] = allPlayers.map(user => user -> calculatePoints(user.handDeck))
    val successfulPoints = usersPoints.collect {case (user, Success(points)) => (user, points)}

    if (successfulPoints.isEmpty) {
      return Seq.empty[User]
    }
    val minPoints = successfulPoints.map(_._2).min
    successfulPoints.collect {
      case (user, points) if points == minPoints => user
    }
  }
}