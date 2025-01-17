package ControllerTest.CORTest
import Controller.Controller
import FileIO.FileIOImpl.FileIOJSON
import _root_.Controller.GameBuilder.GameBuilder
import _root_.Controller.COR.CORImpl.{LifePointsHandler, PotentialSchwimmerHandler, SchwimmerHandler}
import Model.BaseImpl.{Card, CardDeck, GameState, User}
import org.scalatest.wordspec.AnyWordSpec

class PotentialSchwimmerHandlerTest extends AnyWordSpec {

  "PotentialSchwimmerHandler" should {

    "detect potential swimmers of loosers" in {
      val looser1 = User(Seq(Card("Herz", "7"), Card("Pik", "10"), Card("Karo", "K")), 0, "Looser1")
      val looser2 = User(Seq(Card("Kreuz", "7"), Card("Herz", "10"), Card("Pik", "K")), 1, "Looser2")
      val table = User(Seq.empty, -1, "Table")
      val initialGameState = GameState(
        players = Seq(looser1, looser2),
        table = table,
        deck = new CardDeck().shuffleDeck()
      )

      val controller = Controller(new GameBuilder {
        override def returnGameState(): GameState = initialGameState
        override def updateTable(): User = table
        override def createCardDeck(): CardDeck = new CardDeck().shuffleDeck()
      }, new FileIOJSON)

      val potentialSchwimmer = controller.gameState.players.filter { p =>
        p.lifePoints == 0
      }

      assert(potentialSchwimmer.map(_.name) == Seq("Looser1"), "Der Spieler mit 0 Leben sollte ein potentieller Schwimmer sein.")
    }

    "delegate further handling to SchwimmerHandler" in {
      val looser1 = User(Seq(Card("Herz", "7"), Card("Pik", "10"), Card("Karo", "K")), 0, "Looser1")
      val looser2 = User(Seq(Card("Kreuz", "7"), Card("Herz", "10"), Card("Pik", "K")), 1, "Looser2")
      val table = User(Seq.empty, -1, "Table")
      val initialGameState = GameState(
        players = Seq(looser1, looser2),
        table = table,
        deck = new CardDeck().shuffleDeck()
      )

      val controller = Controller(new GameBuilder {
        override def returnGameState(): GameState = initialGameState
        override def updateTable(): User = table
        override def createCardDeck(): CardDeck = new CardDeck().shuffleDeck()
      }, new FileIOJSON)

      val potentialSchwimmer = controller.gameState.players.filter { p =>
        p.lifePoints == 0
      }

      val handler = SchwimmerHandler().handle(controller, potentialSchwimmer)
      assert(handler == (), "Der SchwimmerHandler sollte erfolgreich ausgeführt werden.")
    }
  }
}
