package Controller

import Model.*
import View.TUI

import scala.annotation.tailrec
object GameManage {
  def initializeNewGame(playersName: Seq[String]): GameState = {
    val cardDeck = new CardDeck().shuffleDeck()

    val users: Seq[User] = playersName.map { name =>
      User(handDeck = Seq.empty, livePoints = 3, name = name)
    }

    val userTable = User(handDeck = Seq.empty, livePoints = -1, name = "TheTable")

    val (deckAfterPlayers, usersWithCards) = users.foldLeft((cardDeck, Seq.empty[User])) {
      case ((currentDeck, updatedUsers), user) =>
        val (updatedDeck, updatedUser) = GameLogic.distributeCardsToUser(currentDeck, user)
        (updatedDeck, updatedUsers :+ updatedUser)
    }
    val (finalDeck, tableWithCards) = GameLogic.distributeCardsToUser(deckAfterPlayers, userTable)

    GameState(usersWithCards, tableWithCards, finalDeck, 1)
  }

  @tailrec
  def playGame(currentGame: GameState): GameState = {
    if (currentGame.gameOver) {
      println("Das Spiel ist vorbei!")
      currentGame
    } else {
      // Get the current player based on the round
      val currentPlayerIndex = (currentGame.round - 1) % currentGame.players.size
      val currentPlayer = currentGame.players(currentPlayerIndex)
      
      val newGameStateTUI = new TUI().playerActionHandler(currentPlayer, currentGame)
      val updatedGameState = newGameStateTUI.copy(round = newGameStateTUI.round + 1)
      
      playGame(updatedGameState)
    }
  }
}
