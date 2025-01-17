package ControllerTest.CommandTest
import Controller.GameBuilder.GameBuilder
import Controller.Controller
import _root_.Controller.Command.CommandImpl.TradeAllCommand
import Model.BaseImpl.{Card, CardDeck, GameState, User}
import FileIO.FileIOImpl.FileIOJSON
import org.scalatest.wordspec.AnyWordSpec

class TradeAllCommandTest extends AnyWordSpec {

  "A TradeAllCommand" should {

    "change all player cards with table cards" in {
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

      val tradeAllCommand = TradeAllCommand(controller)
      tradeAllCommand.execute()

      val updatedPlayer = controller.gameState.players.find(_.name == "Player").get
      val updatedTable = controller.gameState.table

      assert(updatedPlayer.handDeck == table.handDeck,"Der Spieler sollte alle Karten vom Tisch haben")
      assert(updatedTable.handDeck == player.handDeck,"Der Tisch sollte alle Karten vom Spieler haben")
    }

    "undo the step" in {
      // TODO
    }

    "redo the step" in {
      // TODO
    }
  }
}
