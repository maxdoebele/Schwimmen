package ControllerTest.CommandTest
import Controller.GameBuilder.GameBuilder
import Controller.Controller
import FileIO.FileIOImpl.FileIOJSON
import Model.BaseImpl.{Card, CardDeck, GameState, User}
import _root_.Controller.Command.CommandImpl.TradeOneCommand
import org.scalatest.wordspec.AnyWordSpec

class TradeOneCommandTest extends AnyWordSpec {

  "A TradeOneCommand" should {

    "change one player card with one table card" in {
      val player = User(Seq(Card("Herz", "7"), Card("Pik", "10"), Card("Karo", "K")), 1, "Player")
      val table = User(Seq(Card("Karo", "8"), Card("Herz", "9"), Card("Kreuz", "A")), -1, "Table")
      val gameState = GameState(
        players = Seq(player),
        table = table,
        deck = new CardDeck().shuffleDeck(),
        indexCardPlayer = 0,
        indexCardTable = 0
      )

      val controller = Controller(new GameBuilder {
        override def returnGameState(): GameState = gameState
        override def updateTable(): User = table
        override def createCardDeck(): CardDeck = new CardDeck().shuffleDeck()
      }, new FileIOJSON)

      val tradeOneCommand = TradeOneCommand(controller)
      tradeOneCommand.execute()

      val updatedPlayer = controller.gameState.players.find(_.name == "Player").get
      val updatedTable = controller.gameState.table

      assert(updatedPlayer.handDeck == Seq(Card("Pik", "10"), Card("Karo", "K"), Card("Karo", "8")), "Der Spieler sollte eine Karte vom Tisch haben")
      assert(updatedTable.handDeck == Seq(Card("Herz", "9"), Card("Kreuz", "A"), Card("Herz", "7")), "Der Tisch sollte eine Karte vom Spieler haben")
    }

    "undo the step" in {
      // TODO
    }

    "redo the step" in {
      // TODO
    }
  }
}
