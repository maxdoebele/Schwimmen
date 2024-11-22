package ControllerTest

import Controller.GameManage._
import Model.CardDeck
import org.scalatest.wordspec.AnyWordSpec

class GameManageTest extends AnyWordSpec {
  
  "The GameMange" should{
    
      "initialize the game with the correct number of players, cards, and table state" in {
        // Arrange
        val playerNames = Seq("Alice", "Bob", "Charlie")
        // Act
        val gameState = initializeNewGame(playerNames)

        // Assert
        // Check that the correct number of players are created
        assert(gameState.players.length == 3, "There should be 3 players in the game")

        // Verify each player has a hand of cards and correct initial live points
        gameState.players.foreach { user =>
          assert(user.handDeck.size == 3, s"${user.name} should have cards in their hand")
          assert(user.livePoints == 3, s"${user.name} should start with 3 live points")
        }

        // Verify the table user is correctly initialized
        val table = gameState.table
        assert(table.name == "Der Tisch", "The table user should be named 'Der Tisch'")
        assert(table.livePoints == -1, "The table should have -1 live points")
        assert(table.handDeck.size == 3, "The table should have cards in its hand")

        // Verify the deck contains the correct number of remaining cards
        val totalCardsInPlay = gameState.players.flatMap(_.handDeck).size + table.handDeck.size
        assert(gameState.deck.cardDeck.size + totalCardsInPlay == 32, "The total number of cards should add up to 52")

        // Verify the game starts at queue 1
        assert(gameState.queue == 1, "The game should start at queue 1")
    }
    
    "loop the game process" in {
      
    }
  }
}
