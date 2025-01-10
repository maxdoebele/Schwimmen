package Controller

import Model._
import Model.BaseImpl._

import scala.util._

object HelpFunctions {

  def calculateCurrentScore(controller: Controller): Map[String, Int] = {
    controller.gameState.players.map(player => {
      player.name -> player.lifePoints
    }).toMap
  }

  def getCurrentPlayer(currentGame: GameStateTrait): User = {
    val currentPlayerIndex = currentGame.queue % currentGame.players.size
    val currentPlayer = currentGame.players(currentPlayerIndex)
    currentPlayer
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

  def findLoserOfRound(allPlayers: Seq[User]): Seq[User] = {
    val usersPoints: Seq[(User, Try[Double])] = allPlayers.map(user => user -> calculatePoints(user.handDeck))
    val successfulPoints = usersPoints.collect { case (user, Success(points)) => (user, points) }

    if (successfulPoints.isEmpty) {
      return Seq.empty[User]
    }

    val playerWith33Points = successfulPoints.find { case (_, points) => points == 33 }

    playerWith33Points match {
      case Some((_, _)) =>
        successfulPoints.collect { case (user, _) if !playerWith33Points.exists(_._1 == user) => user }
      case None =>
        val minPoints = successfulPoints.map(_._2).min
        successfulPoints.collect {
          case (user, points) if points == minPoints => user
        }
    }
  }

}