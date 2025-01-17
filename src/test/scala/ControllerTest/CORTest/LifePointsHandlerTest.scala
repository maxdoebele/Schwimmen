package ControllerTest.CORTest
import Controller.Controller
import _root_.Controller.GameBuilder.GameBuilder
import _root_.Controller.COR.CORImpl.{LifePointsHandler, PotentialSchwimmerHandler}
import Model.BaseImpl.{Card, CardDeck, GameState, User}
import FileIO.FileIOImpl.FileIOJSON
import org.scalatest.wordspec.AnyWordSpec

class LifePointsHandlerTest extends AnyWordSpec {

  "LifePointsHandler" should {

    "reduce life points of losing players and update the game state" in {
      val player1 = User(Seq(Card("Herz", "7"), Card("Pik", "10"), Card("Karo", "K")), 2, "Player1")
      val player2 = User(Seq(Card("Kreuz", "7"), Card("Herz", "10"), Card("Herz", "K")), 3, "Player2")
      val table = User(Seq.empty, -1, "Table")
      val initialGameState = GameState(
        players = Seq(player1, player2),
        table = table,
        deck = new CardDeck().shuffleDeck()
      )
      val controller = Controller(new GameBuilder {
        override def returnGameState(): GameState = initialGameState
        override def updateTable(): User = table
        override def createCardDeck(): CardDeck = new CardDeck().shuffleDeck()
      }, new FileIOJSON)

      val loosers = Seq(player1)

      LifePointsHandler().handle(controller, loosers)

      val updatedPlayer1 = controller.gameState.players.find(_.name == "Player1").get
      val updatedPlayer2 = controller.gameState.players.find(_.name == "Player2").get

      assert(updatedPlayer1.lifePoints == 1, "Player1 sollte ein Leben verloren haben")
      assert(updatedPlayer2.lifePoints == 3, "Player2 sollte unverändert bleiben")
    }

    "delegate further handling to PotentialSchwimmerHandler" in {
      
      val player1 = User(Seq(Card("Herz", "7"), Card("Pik", "10"), Card("Karo", "K")), 1, "Player1")
      val player2 = User(Seq(Card("Kreuz", "7"), Card("Herz", "10"), Card("Herz", "K")), 3, "Player2")
      val table = User(Seq.empty, -1, "Table")
      val gameState = GameState(
        players = Seq(player1, player2),
        table = table,
        deck = new CardDeck().shuffleDeck()
      )
      val controller = Controller(new GameBuilder {
        override def returnGameState(): GameState = gameState
        override def updateTable(): User = table
        override def createCardDeck(): CardDeck = new CardDeck().shuffleDeck()
      }, new FileIOJSON)
      
      val loosers = Seq(player1)
      
      val handler = PotentialSchwimmerHandler().handle(controller, loosers)
      assert(handler == (), "Der PotentialSchwimmerHandler sollte erfolgreich ausgeführt werden.")
    }
  }
}
