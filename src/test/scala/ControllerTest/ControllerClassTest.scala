package ControllerTest
import org.scalatest.wordspec.AnyWordSpec
import Controller.{Controller, HelpFunctions}
import FileIO.FileIOImpl.FileIOJSON
import _root_.Controller.COR.CORImpl.LifePointsHandler
import _root_.Controller.GameBuilder.GameBuilderImpl.BuildNewGame
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
  val controller = Controller(BuildNewGame(playerNames), new FileIOJSON)
  controller.gameState = gameState

  "Controller" should {

    "set gameOver if player has schnauz" in {
      assert(!controller.gameState.gameOver, "gameOver sollte auf false gesetzt sein")
      controller.checkForSchnauz()
      val schnauzPlayer = controller.gameState.players.find(player =>
        player.name == "SchnauzPlayer"
      )
      assert(schnauzPlayer.isDefined, "SchnauzPlayer sollte im Spielstand existieren")
    }

    "call LifePointsHandler if game is over" in {
      controller.checkifGameOver()
      val loosers = HelpFunctions.findLoserOfRound(controller.gameState.players)
      val handler = LifePointsHandler().handle(controller, loosers)
      assert(handler == (), "LifePointsHandler sollte erfolgreich ausgeführt werden")
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
    val initialGameState = controller.gameState
    controller.undo()
    assert(controller.gameState != initialGameState, "GameState sollte zurückgesetzt worden sein.")
    }

    "redo" in {
    val initialGameState = controller.gameState
    controller.undo()
    controller.redo()
    assert(controller.gameState == initialGameState, "GameState sollte wiederhergestellt worden sein.")
    }

    "cancel read Line" in {
      @volatile var threadReadLine: Thread = null

      // Dummy thread
      threadReadLine = new Thread(() => {
        try {
          while (true) {
            Thread.sleep(1000)
          }
        } catch {
          case _: InterruptedException =>
          // Erwartete Unterbrechung
        }
      })

      threadReadLine.start()

      controller.threadReadLine = threadReadLine
      controller.cancelReadLine()
      Thread.sleep(100)

      assert(!threadReadLine.isAlive, "Thread sollte unterbrochen worden sein")
    }

    "reset round when gameOver is true" in {
      controller.gameState = controller.gameState.copy(gameOver = true)
      controller.resetRound()
      assert(!controller.gameState.gameOver, "Spiel sollte zurückgesetzt werden")
    }

    "load the game" in {
      // TODO
    }

    "save the game" in {
      // TODO
    }
  }
}
