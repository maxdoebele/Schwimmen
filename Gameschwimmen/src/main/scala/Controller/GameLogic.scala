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


}