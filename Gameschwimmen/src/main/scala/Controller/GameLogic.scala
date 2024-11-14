package Controller

import Model.{Card, GameState, User}

import scala.io.StdIn.readLine
import scala.util.Random

object GameLogic {
  def distributeCardsToUser(deck: Seq[Card], user: User): (Seq[Card], User) = {
    val (drawnCards, newDeck) = remove3Cards(deck)
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
    val updatedGameState = GameState(gameState.players, gameState.table, gameState.deck, gameState.round, gameState.knockCounter + 1, gameState.gameOver)
    if (!updatedGameState.gameOver) {
      gameState
    } else {
      //endGamefunction wird aufgerufen GameManage
      gameState
    }
  }

  def makeNewDeck: Seq[Card] = {
    for {
      suit <- Seq("Herz", "Pik", "Karo", "Kreuz")
      rank <- Seq("7", "8", "9", "10", "J", "Q", "K", "A")
    } yield Card(suit, rank)

  }

  def shuffleDeck(aDeck: Seq[Card]): Seq[Card] = {
    Random.shuffle(aDeck)
  }

  def remove3Cards(aDeck: Seq[Card]): (Seq[Card], Seq[Card]) = {
    if (aDeck.length < 3) {
      throw new IllegalStateException("Not enough cards in the deck to remove 3.")
    }
    val (onHoldCard, remaining) = aDeck.splitAt(3)
    (onHoldCard, remaining)
  }
}
