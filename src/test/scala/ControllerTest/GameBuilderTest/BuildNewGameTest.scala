package ControllerTest.GameBuilderTest
import Controller.GameBuilder.GameBuilderImpl._
import org.scalatest.wordspec.AnyWordSpec

class BuildNewGameTest extends AnyWordSpec {

  "BuildNewGame" should {
    "create a carddeck" in {
      val player = Seq("Player1", "Player2", "Player3", "Player4")
      val cardeck = BuildNewGame(player).createCardDeck()
      assert(cardeck.cardDeck.size == 32, "The carddeck should have 32 cards.")
    }
    "create players" in {
      val player = Seq("Player1", "Player2", "Player3", "Player4")
      val players = BuildNewGame(player).createPlayers(player)
      assert(players.size == 4, "The players should be 4.")
      assert(players.map(_.name) == player, "The players should have the names Player1, Player2, Player3, Player4.")
      assert(players.map(_.lifePoints) == Seq(3, 3, 3, 3), "The players should have 3 life points.")
    }
    "create a table" in {
      val player = Seq("Player1", "Player2", "Player3", "Player4")
      val table = BuildNewGame(player).updateTable()
      assert(table.name == "theTable", "The table should have the name theTable.")
      assert(table.lifePoints == -1, "The table should have -1 life points.")
    }
    "return a gamestate" in {
      val player = Seq("Player1", "Player2", "Player3", "Player4")
      val gameState = BuildNewGame(player).returnGameState()
      assert(gameState.players.size == 4, "The gamestate should have 4 players.")
      assert(gameState.table.name == "theTable", "The table should have the name theTable.")
      assert(gameState.table.lifePoints == -1, "The table should have -1 life points.")
      assert(gameState.deck.cardDeck.size == 32 - 5 * 3, "The carddeck should have 32 cards.")
    }
  }
}
