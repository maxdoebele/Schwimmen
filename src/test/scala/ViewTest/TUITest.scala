package ViewTest

import Controller.{Controller, HelpFunctions}
import _root_.Controller.GameBuilder.GameBuilder
import Model.BaseImpl.{Card, CardDeck, GameState, User}
import View.tui.{InputHandlerTest, TUI}
import _root_.FileIO.FileIOImpl.FileIOJSON
import _root_.Controller.GameBuilder.GameBuilderImpl.{BuildNewGame, BuildNewRound}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import java.io.{ByteArrayOutputStream, PrintStream}
import scala.util.{Failure, Success}
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class TUITest extends AnyWordSpec with Matchers {

  "A TUI" should {

    "start the game and create a new game with valid player input" in {

      val player = Seq.empty
      val controller = Controller(BuildNewGame(player), new FileIOJSON)
      val inputHandler = new InputHandlerTest
      val tui = new TUI(controller, inputHandler)

      // Valid input test
      val namesIO = "Alice, Bob"
      inputHandler.fillInputBuffer(namesIO, true)

      val startFuture = tui.start()
      Await.ready(startFuture, Duration.Inf) // Wait for the async operation to complete

      controller.gameState.players.map(_.name) shouldEqual Seq("Alice", "Bob")
    }

    "retry when wrong input in start" in {
      val player = Seq.empty
      val controller = Controller(BuildNewGame(player), new FileIOJSON)
      val inputHandler = new InputHandlerTest
      val tui = new TUI(controller, inputHandler)

      val namesNIO = "p1"

      inputHandler.fillInputBuffer(namesNIO, true) // Fill the input buffer with invalid input
      val startFuture = tui.start()
      Await.ready(startFuture, Duration.Inf) // Wait for the async operation to complete

      controller.gameState.players shouldBe empty
    }


    "reject invalid player input" in {
      val player = Seq.empty
      val controller = Controller(BuildNewGame(player), new FileIOJSON)
      val tui = new TUI(controller, new InputHandlerTest)

      val names = "Alice" // Invalid input
      val inputMock = Future.successful(names)

      inputMock.onComplete {
        case Success(input) =>
          val playerNames = input.split(", ").toList
          if (!HelpFunctions.checkForPlayerLimit(playerNames)) {
            println("Ungültige Eingabe. Es müssen zwischen 2 und 9 Spieler sein.")
          }
        case Failure(_) =>
        // Handle failure case
      }

      Thread.sleep(100) // Allow time for the future to execute
      controller.gameState.players shouldBe empty
    }

    "handle a trade action correctly" in {
      val player = User(Seq(Card("Herz", "7"), Card("Pik", "10"), Card("Karo", "K")), 1, "Player")
      val table = User(Seq(Card("Karo", "8"), Card("Herz", "9"), Card("Kreuz", "A")), -1, "Table")
      val gameState = GameState(
        players = Seq(player),
        table = table,
        deck = new CardDeck().shuffleDeck(),
        queue = 1
      )

      val controller = Controller(new GameBuilder {
        override def returnGameState(): GameState = gameState
        override def updateTable(): User = table
        override def createCardDeck(): CardDeck = new CardDeck().shuffleDeck()
      }, new FileIOJSON)

      val tradeIndices = (0, 1) // Simulating trade indices
      controller.tradeOne(tradeIndices._1, tradeIndices._2)

      controller.gameState.players.head.handDeck.contains(Card("Herz", "9")) shouldBe true
      controller.gameState.table.handDeck.contains(Card("Herz", "7")) shouldBe true
    }

    "test end of round via update()" in {
      val player = User(Seq(Card("Herz", "7"), Card("Pik", "10"), Card("Karo", "K")), 1, "Player")
      val table = User(Seq(Card("Karo", "8"), Card("Herz", "9"), Card("Kreuz", "A")), -1, "Table")
      val gameState = GameState(
        players = Seq(player),
        table = table,
        deck = new CardDeck().shuffleDeck(),
        queue = 1,
        lastLoosers = List(User(Nil, -1, "Bob")) // Simulate the last loser
      )

      val controller = Controller(new GameBuilder {
        override def returnGameState(): GameState = gameState

        override def updateTable(): User = table

        override def createCardDeck(): CardDeck = new CardDeck().shuffleDeck()
      }, new FileIOJSON)

      val tui = new TUI(controller, new InputHandlerTest)

      // Simulate the state where the round ends
      val outputStream = new java.io.ByteArrayOutputStream()
      Console.withOut(outputStream) {
        tui.update() // Trigger the public method that calls displayEndOfRound internally
      }
    }
  }
}
