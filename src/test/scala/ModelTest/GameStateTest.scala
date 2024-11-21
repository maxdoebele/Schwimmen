package ModelTest

import Model.{Card, CardDeck, GameState, User}
import org.scalatest.wordspec.AnyWordSpec

class GameStateTest extends AnyWordSpec {

  "A GameState" should {

    "initialize correctly with the given players, table, and deck" in {
      val player1 = User(Seq(Card("Herz", "7")), 3, "Max")
      val player2 = User(Seq(Card("Pik", "8")), 3, "Emilia")
      val table = User(Seq(Card("Karo", "9")), 3, "Table")
      val deck = new CardDeck()

      val gameState = GameState(Seq(player1, player2), table, deck)

      assert(gameState.players.size == 2)
      assert(gameState.players.contains(player1))
      assert(gameState.players.contains(player2))
      assert(gameState.table == table)
      assert(gameState.deck == deck)
    }

    "initialize with default values for round, knockCounter, and gameOver" in {
      val player1 = User(Seq(Card("Herz", "7")), 3, "Max")
      val table = User(Seq(Card("Pik", "8")), 3, "Table")
      val deck = new CardDeck()

      val gameState = GameState(Seq(player1), table, deck)

      assert(gameState.round == 0)
      assert(gameState.knockCounter == 0)
      assert(!gameState.gameOver)
    }

    "update the round number correctly when incremented" in {
      val player1 = User(Seq(Card("Herz", "7")), 3, "Max")
      val table = User(Seq(Card("Pik", "8")), 3, "Table")
      val deck = new CardDeck()

      val gameState = GameState(Seq(player1), table, deck)

      assert(gameState.round == 0)

      val updatedGameState = gameState.copy(round = gameState.round + 1)
      assert(updatedGameState.round == 1)
    }

    "correctly handle the gameOver state" in {
      val player1 = User(Seq(Card("Herz", "7")), 3, "Max")
      val table = User(Seq(Card("Pik", "8")), 3, "Table")
      val deck = new CardDeck()

      val gameState = GameState(Seq(player1), table, deck)

      assert(!gameState.gameOver)

      val updatedGameState = gameState.copy(gameOver = true)
      assert(updatedGameState.gameOver)
    }

    "handle knockCounter correctly" in {
      val player1 = User(Seq(Card("Herz", "7")), 3, "Max")
      val table = User(Seq(Card("Pik", "8")), 3, "Table")
      val deck = new CardDeck()

      val gameState = GameState(Seq(player1), table, deck)

      assert(gameState.knockCounter == 0)

      val updatedGameState = gameState.copy(knockCounter = gameState.knockCounter + 1)
      assert(updatedGameState.knockCounter == 1)
    }
  }
}

