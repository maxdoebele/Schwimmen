package ControllerTest.GameBuilderTest

import Controller.GameBuilder.GameBuilderImpl.{BuildNewGame, BuildNewRound}
import Model.*
import Model.BaseImpl.{Card, CardDeck, GameState, User}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import Controller.Controller

class BuildNewRoundTest extends AnyWordSpec with Matchers {

  "BuildNewRound" should {

    "create and shuffle a new card deck" in {
      val player = Seq("Player1", "Player2", "Player3", "Player4")
      val gameState = BuildNewGame(player).returnGameState()

      val builder = new BuildNewRound(gameState)
      val deck = builder.createCardDeck()

      deck.cardDeck should not be empty
      deck.cardDeck.size shouldBe 32
    }
    "update players by filtering out players with zero life points and rotating" in {
      val players = Seq(
        User(handDeck = Seq.empty, lifePoints = 3, name = "Player1"),
        User(handDeck = Seq.empty, lifePoints = 0, name = "Player2"),
        User(handDeck = Seq.empty, lifePoints = 1, name = "Player3")
      )

      val player = Seq("Player1", "Player2", "Player3", "Player4")
      val gameState = BuildNewGame(player).returnGameState()

      val builder = new BuildNewRound(gameState)
      val updatedPlayers = builder.updatePlayers(players)

      // Verify that only players with life points > 0 are included
      updatedPlayers should have size 2
      updatedPlayers.map(_.name) should contain theSameElementsInOrderAs Seq("Player3", "Player1")
    }
    "update table" in {
      val player = Seq("Player1", "Player2", "Player3", "Player4")
      val gameState = BuildNewGame(player).returnGameState()

      val builder = new BuildNewRound(gameState)
      val table = builder.updateTable()

      table.name shouldBe "theTable"
      table.lifePoints shouldBe -1
      table.handDeck shouldBe empty
    }

    "return an updated game state with a new round" in {
      val players = Seq(
        User(handDeck = Seq(Card("Herz", "8"), Card("Kreuz", "8"), Card("Kreuz", "8")), lifePoints = 2, name = "Player1"),
        User(handDeck = Seq(Card("Herz", "7"), Card("Kreuz", "10"), Card("Kreuz", "K")), lifePoints = 0, name = "Player2"),
        User(handDeck = Seq(Card("Pik", "A"), Card("Pik", "10"), Card("Pik", "K")), lifePoints = 3, name = "Player3")
      )

      val player = Seq("Player1", "Player2", "Player3")
      val gameState = BuildNewGame(player).returnGameState()
      val gameStateWithPlayers = gameState.copy(players = players)

      val builder = new BuildNewRound(gameStateWithPlayers)
      val updatedGameState = builder.returnGameState()

      updatedGameState.roundCounter shouldBe 1
      updatedGameState.players.size shouldBe 2 // Player with 0 life points should be removed
      updatedGameState.table.name shouldBe "theTable"
      updatedGameState.deck.cardDeck should not be empty
      updatedGameState.playerPoints should contain theSameElementsInOrderAs Seq(30.5, 20.0, 31.0)
    }
  }
}

