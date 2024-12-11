package Controller

import Controller.util.Controller
import Model._

import scala.util.Try

object HelpFunctions {

  def calculatePoints(cards: Seq[Card]): Try[Double] = Try {
    val halbe = 30.5
    val schnauz = 31
    val feuer = 33

    if (cards.size != 3) throw new IllegalArgumentException("Genau drei Karten erwartet.")

    if (cards.map(_.rank).distinct.size == 1) {
      if (cards.head.rankToPoints * 3 == feuer)
        feuer
      else
        halbe
    } else {
      val groupedBySuit = cards.groupBy(_.suit)
      val sameSuitGroup = groupedBySuit.values.find(_.size > 1)

      sameSuitGroup match {
        case Some(sameSuitCards) =>
          sameSuitCards.map(_.rankToPoints).sum // Sum points of cards with the same suit
        case None =>
          cards.map(_.rankToPoints).max // All cards have different suits, return the highest rank points
      }
    }
  }

  def calculateCurrentScore(controller: Controller): Map[String, Int] = {
    controller.gameState.players.map(player => {
      player.name -> player.livePoints
    }).toMap
  }


  def checkForSchnauz(controller: Controller): Unit = {
    val schnauzPlayer = controller.gameState.players.find(player => calculatePoints(player.handDeck).getOrElse(0.0) == 31)
    val feuerSchnautzPlayer = controller.gameState.players.find(player => calculatePoints(player.handDeck).getOrElse(0.0) == 33)

    if (schnauzPlayer.isDefined || feuerSchnautzPlayer.isDefined) {
      controller.gameState = controller.gameState.copy(gameOver = true)
    }
  }


  def getCurrentPlayer(currentGame: GameState): User = {
    val currentPlayerIndex = currentGame.queue % currentGame.players.size
    val currentPlayer = currentGame.players(currentPlayerIndex)
    currentPlayer
  }

  def updateLivePoints(controller: Controller, losers: Seq[User]): Unit = {
    val updatedPlayers = if (controller.gameState.schwimmer) {
      controller.gameState.players.map { player =>
        if (losers.exists(_.name == player.name)) {
          player.loseLivePoint()
        } else {
          player
        }
      }
    } else {
      controller.gameState.players.map { player =>
        if (losers.exists(_.name == player.name)) {
          if (player.livePoints == 1) {
            controller.gameState = controller.gameState.copy(schwimmer = true)
            player.setSchwimmer()
          } else {
            player.loseLivePoint()
          }
        } else {
          player
        }
      }
    }

    controller.gameState = controller.gameState.copy(players = updatedPlayers)
  }
}