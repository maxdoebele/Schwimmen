package Controller

import Model._

import scala.io.StdIn.readLine
import scala.util.Random

object GameLogic {
  def distributeCardsToUser(deck: CardDeck, user: User): (CardDeck, User) = {
    val (drawnCards, newDeck) = deck.remove3Cards()
    val updatedUser = user.add3Cards(drawnCards)
    (newDeck, updatedUser)
  }

  def knock(gameState: GameState): GameState = {
    if (gameState.knockCounter + 1 >= 2) {
      gameState.copy(knockCounter = gameState.knockCounter + 1, gameOver = true)
    } else {
      gameState.copy(knockCounter = gameState.knockCounter + 1)
    }
  }

  def trade(gameState: GameState, indexPlayer: Int, indexTable: Int, currentPlayer: User): GameState = {
    val Some((userWithTwoCards, playerCard)) = currentPlayer.removeCard(indexPlayer)
    val Some((tableWithTwoCards, tableCard)) = gameState.table.removeCard(indexTable)
    val userWithThreeCards = userWithTwoCards.addCard(tableCard)
    val tableWithThreeCards = tableWithTwoCards.addCard(playerCard)
    val updatedPlayers = gameState.players.map { player =>
      if (player.name == currentPlayer.name) userWithThreeCards else player
    }
    gameState.copy(players = updatedPlayers, table = tableWithThreeCards)
  }

  def calculatePoints(cards: Seq[Card]): Double = {
    val halbe = 30.5
    val schnauz = 31
    val feuer = 33

    if (cards.size != 3) throw new IllegalArgumentException("Exactly 3 cards are required.")

    // Check if all ranks are the same
    if (cards.map(_.rank).distinct.size == 1) {
      if (cards.head.rankToPoints * 3 == feuer)
        return feuer
      return halbe // Triple the points for the rank
    }

    val groupedBySuit = cards.groupBy(_.suit) // Group cards by their suits
    val sameSuitGroup = groupedBySuit.values.find(_.size > 1)

    sameSuitGroup match {
      case Some(sameSuitCards) =>
        sameSuitCards.map(_.rankToPoints).sum // Sum points of cards with the same suit
      case None =>
        cards.map(_.rankToPoints).max // All cards have different suits, return the highest rank points
    }
  }

  def checkForSchnauz(gameState: GameState): GameState = {
    val schnauzPlayer = gameState.players.find(player => calculatePoints(player.handDeck) == 31)
    val over33Player = gameState.players.find(player => calculatePoints(player.handDeck) == 33)

    (schnauzPlayer, over33Player) match {
      case (Some(player), _) =>
        println(s"${player.name} hat Schnauz (31 Punkte)!")
        gameState.copy(gameOver = true) // Spiel ist vorbei, Schnauz erreicht
      case (_, Some(player)) =>
        println(s"${player.name} hat 33 Punkte erreicht!")
        gameState.copy(gameOver = true) // Spiel ist vorbei, 33 Punkte erreicht
      case _ =>
        gameState // Keine besonderen Punkte erreicht, Spiel geht weiter
    }
  }

}