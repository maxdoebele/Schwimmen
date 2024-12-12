package Controller

import Controller.util.Controller
import Model._

import scala.util._

object HelpFunctions {

  def findLoserOfRound(allPlayers: Seq[User]): Seq[User] = {
    val usersPoints: Seq[(User, Try[Double])] = allPlayers.map(user => user -> calculatePoints(user.handDeck))
    val successfulPoints = usersPoints.collect { case (user, Success(points)) => (user, points) }

    if (successfulPoints.isEmpty) {
      return Seq.empty[User]
    }
    val minPoints = successfulPoints.map(_._2).min
    successfulPoints.collect {
      case (user, points) if points == minPoints => user
    }
  }

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
      player.name -> player.lifePoints
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

}