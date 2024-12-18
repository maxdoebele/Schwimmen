package Controller

import Controller.util.Controller
import _root_.Controller.GameBuilder.BuildNewRound
import Model.*

import scala.util.*

object HelpFunctions {

  def calculateCurrentScore(controller: Controller): Map[String, Int] = {
    controller.gameState.players.map(player => {
      player.name -> player.lifePoints
    }).toMap
  }

  def getCurrentPlayer(currentGame: GameState): User = {
    val currentPlayerIndex = currentGame.queue % currentGame.players.size
    val currentPlayer = currentGame.players(currentPlayerIndex)
    currentPlayer
  }

}