package ControllerTest
import org.scalatest.wordspec.AnyWordSpec
import Controller.Controller
import Controller.GameBuilder.GameBuilderImpl.BuildNewGame
import _root_.Controller.GameBuilder.GameBuilder
import Model.BaseImpl.{Card, CardDeck, GameState, User}

class ControllerClassTest extends AnyWordSpec {

  val player1 = User(Seq(Card("Herz", "7"), Card("Pik", "10"), Card("Karo", "K")), 2, "Player1")
  val player2 = User(Seq(Card("Kreuz", "7"), Card("Herz", "10"), Card("Herz", "K")), 3, "Player2")
  val schnauzPlayer = User(Seq(Card("Herz", "A"), Card("Herz", "J"), Card("Herz", "D")), 2, "SchnauzPlayer")
  val table = User(Seq(Card("Karo", "8"), Card("Herz", "9"), Card("Kreuz", "A")), -1, "Table")
  val gameState = GameState(
  players = Seq(player1, player2, schnauzPlayer),
  table = table,
  deck = new CardDeck().shuffleDeck(),
  )
  val playerNames = Seq.empty
  val controller = Controller(BuildNewGame(playerNames))
  controller.gameState = gameState

  "Controller" should {

    "set gameOver if player has schnauz" in {
      controller.checkForSchnauz(controller)
      assert(controller.gameState.gameOver, "in GameState sollte gameOver auf true gesetzt werden")
    }

    "create a new Game" in {
      val playerNames = Seq("Alice", "Bob", "Charlie")
      controller.createNewGame(playerNames)
      val players = controller.gameState.players
      assert(players.map(_.name) == playerNames, "neues Spiel sollte mit Namen erstellt werden")
    }

    "call knock" in {
    val initialGameState = controller.gameState
    controller.knock()
    assert(controller.gameState.knockCounter == initialGameState.knockCounter + 1, "KnockCounter sollte um 1 erhöht werden")
    }

    "call skip" in {
    val initialGameState = controller.gameState
    controller.skip()
    assert(controller.gameState.queue == initialGameState.queue + 1, "Queue sollte um 1 erhöht werden")
    }

    "call trade All" in {
    val initialGameState = controller.gameState
    controller.tradeAll()
    assert(controller.gameState != initialGameState, "Spieler sollte alle Karten vom Tisch haben")
    }

    "call trade One" in {
    val initialGameState = controller.gameState
    controller.tradeOne(0,0)
    assert(controller.gameState != initialGameState, "GameState sollte sich ändern")
    }

    "undo" in {
    // TODO
    }

    "redo" in {
    // TODO
    }

    "reset round when gameOver is true" in {
      controller.gameState = controller.gameState.copy(gameOver = true)
      controller.resetRound()
      assert(!controller.gameState.gameOver, "Spiel sollte zurückgesetzt werden")
    }
  }
}
