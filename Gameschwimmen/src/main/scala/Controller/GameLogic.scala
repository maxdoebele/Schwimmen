package Controller

import Model.{Card, CardDeck, GameState, User}

import scala.io.StdIn.readLine

object GameLogic {
  def distributeCardsToUser(deck: CardDeck, user: User): (CardDeck, User) = {
    val (drawnCards, newDeck) = deck.remove3Cards()
    val updatedUser = user.add3Cards(drawnCards)
    (newDeck, updatedUser)
  }

  def playTurn(player: User, gameState: GameState): GameState = {
    println(s"${player.name}, it's your turn! Choose an action: 1 = Knock, 2 = Skip, 3 = Trade")
    val action = readLine("Enter your number: ") match {
      case "1" =>
        knock(gameState)
        println(s"Spieler ${player.name} hat geklopft")
      case "2" =>
        //skip()
        println(s"Spieler ${player.name} hat geschoben")
      case "3" =>
        //trade()
        println(s"Spieler ${player.name} hat getauscht")
      case _ =>
        println("Invalid action. Please try again.")
        playTurn(player, gameState) // Retry on invalid input
    }
    gameState
  }
  def knock(gameState: GameState): GameState = {
    gameState.knockCount()
    if (!gameState.gameOver) {
      gameState
    } else {
      //endGamefunction wird aufgerufen GameManage
      gameState
    }
  }
}
