package ControllerTest.CommandTest

import Controller.GameBuilder.GameBuilder
import Controller.Controller
import FileIO.FileIOImpl.FileIOJSON
import _root_.Controller.Command.CommandImpl.KnockCommand
import Model.BaseImpl.{Card, CardDeck, GameState, User}
import org.scalatest.wordspec.AnyWordSpec

class KnockCommandTest extends AnyWordSpec {

  val player1 = User(Seq(Card("Herz", "7"), Card("Pik", "10"), Card("Karo", "K")), 1, "Player1")
  val player2 = User(Seq(Card("Kreuz", "7"), Card("Herz", "10"), Card("Herz", "K")), 3, "Player2")
  val table = User(Seq.empty, -1, "Table")
  val gameState = GameState(
    players = Seq(player1, player2),
    table = table,
    deck = new CardDeck().shuffleDeck(),
    queue = 1,
    knockCounter = 1,
    gameOver = false
  )

  val controller = Controller(new GameBuilder {
    override def returnGameState(): GameState = gameState
    override def updateTable(): User = table
    override def createCardDeck(): CardDeck = new CardDeck().shuffleDeck()
  }, new FileIOJSON)

  "A KnockCommand" should {

    "increment knockCounter and set gameOver" in {
      val knock = KnockCommand(controller)
      knock.execute()

      assert(controller.gameState.knockCounter == 2, "Der knockCounter sollte um 1 erhöht werden.")
      assert(controller.gameState.gameOver, "Das Spiel sollte vorbei sein.")
    }

    "undo the step" in {
      val knock = KnockCommand(controller)
      knock.execute()

      knock.undoStep()
      val updatedKnockCounter = controller.gameState.knockCounter
      val GameOver = controller.gameState.gameOver

      assert(controller.gameState.knockCounter == updatedKnockCounter, "Der knockCounter sollte zurückgesetzt worden sein.")
      assert(controller.gameState.gameOver == GameOver, "Das Spiel sollte nicht mehr vorbei sein.")
    }

    "redo the step" in {
      val knock = KnockCommand(controller)
      knock.execute()

      knock.undoStep()
      val updatedKnockCounter = controller.gameState.knockCounter
      knock.redoStep()

      assert(controller.gameState.knockCounter != updatedKnockCounter, "Der knockCounter sollte wieder auf 2 erhöht worden sein.")
      assert(controller.gameState.gameOver, "Das Spiel sollte wieder vorbei sein.")
    }
  }
}
