package ControllerTest.CommandTest
import Controller.Controller
import FileIO.FileIOImpl.FileIOJSON
import _root_.Controller.Command.CommandImpl.SkipCommand
import _root_.Controller.GameBuilder.GameBuilder
import _root_.Controller.COR.CORImpl.{LifePointsHandler, PotentialSchwimmerHandler}
import Model.BaseImpl.{Card, CardDeck, GameState, User}
import FileIO.FileIOImpl.FileIOJSON
import org.scalatest.wordspec.AnyWordSpec

class SkipCommandTest extends AnyWordSpec {

  val player1 = User(Seq(Card("Herz", "7"), Card("Pik", "10"), Card("Karo", "K")), 1, "Player1")
  val player2 = User(Seq(Card("Kreuz", "7"), Card("Herz", "10"), Card("Herz", "K")), 3, "Player2")
  val table = User(Seq.empty, -1, "Table")
  val gameState = GameState(
    players = Seq(player1, player2),
    table = table,
    deck = new CardDeck().shuffleDeck(),
    queue = 1
  )

  val controller = Controller(new GameBuilder {
    override def returnGameState(): GameState = gameState

    override def updateTable(): User = table

    override def createCardDeck(): CardDeck = new CardDeck().shuffleDeck()
  }, new FileIOJSON)
  
  "A SkipCommand" should {

    "increment queue" in {
      val skip = SkipCommand(controller)
      skip.execute()

      assert(controller.gameState.queue == 2, "Der nächste Spieler sollte dran sein.")
    }

    "undo the step" in {
      val skip = SkipCommand(controller)
      skip.execute()

      val currentState = controller.gameState

      skip.undoStep()

      assert(controller.gameState.queue != currentState.queue , "Der gleiche Spieler sollte wieder dran sein.")
    }

    "redo the step" in {
      val skip = SkipCommand(controller)
      skip.execute()

      val currentState = controller.gameState

      skip.undoStep()
      skip.redoStep()

      assert(controller.gameState.queue == currentState.queue, "Der nächste Spieler sollte wieder dran sein.")
    }
  }
}
