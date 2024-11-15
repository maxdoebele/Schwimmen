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
    val updatedGameState = GameState(gameState.players, gameState.table, gameState.deck, gameState.round, gameState.knockCounter + 1, gameState.gameOver)
    updatedGameState
  }

}