package Controller.Command

import Controller.UpdateGameState.updateGameState
import Model.*

object PlayerActions {

  def skipAction(currentPlayer: User, gameState: GameState): GameState = {
    updateGameState(gameState)
  }

  def knockAction(currentPlayer: User, gameState: GameState): GameState = {
    val newKnockCounter = gameState.knockCounter + 1
    updateGameState(gameState, knockCounter = Some(newKnockCounter), gameOver = Some(newKnockCounter >= 2)
    )
  }

  def tradeAllAction(currentPlayer: User, gameState: GameState): GameState = {
    val (userWithNoCards, playerCards) = currentPlayer.removeAllCards()
    val (tableWithNoCards, tableCards) = gameState.table.removeAllCards()
    val userWithNewCards = userWithNoCards.add3Cards(tableCards)
    val tableWithNewCards = tableWithNoCards.add3Cards(playerCards)
    updateGameState(gameState, Some(currentPlayer), Some(userWithNewCards), Some(tableWithNewCards))
  }

  def tradeOneAction(currentPlayer: User, gameState: GameState, playerIndex: Int, tableIndex: Int): GameState = {
    val Some((userWithTwoCards, playerCard)) = currentPlayer.removeCard(playerIndex)
    val Some((tableWithTwoCards, tableCard)) = gameState.table.removeCard(tableIndex)
    val userWithThreeCards = userWithTwoCards.addCard(tableCard)
    val tableWithThreeCards = tableWithTwoCards.addCard(playerCard)
    updateGameState(gameState, Some(currentPlayer), Some(userWithThreeCards), Some(tableWithThreeCards))
  }

}
