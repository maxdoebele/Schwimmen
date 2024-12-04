package Controller

import Model._

object HelpFunctions {

  def calculatePoints(cards: Seq[Card]): Double = {
    val halbe = 30.5
    val schnauz = 31
    val feuer = 33

    if (cards.size != 3) throw new IllegalArgumentException("Genau drei Karten erwartet.")

    // Check if all ranks are the same
    if (cards.map(_.rank).distinct.size == 1) {
    if (cards.head.rankToPoints * 3 == feuer)
      return feuer 
    else 
    return halbe
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
    val feuerSchnautzPlayer = gameState.players.find(player => calculatePoints(player.handDeck) == 33)

    (schnauzPlayer, feuerSchnautzPlayer) match {
      case (Some(player), _) =>
        val updatedGameState = gameState.copy(gameOver = true)
        updatedGameState
      case _ =>
        gameState
    }
  }

  def getCurrentPlayer(currentGame: GameState): User = {
    val currentPlayerIndex = currentGame.queue % currentGame.players.size
    val currentPlayer = currentGame.players(currentPlayerIndex)
    currentPlayer
  }

  def updateLivePoints(gameState: GameState, losers: Seq[User]): GameState = {
    val updatedPlayers = gameState.players.map { player =>
      if (losers.exists(_.name == player.name)) {
        player.loseLivePoint()
      } else {
        player
      }
    }
    gameState.copy(players = updatedPlayers)
  }
}