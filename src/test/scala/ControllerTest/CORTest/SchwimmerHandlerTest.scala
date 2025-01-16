package ControllerTest.CORTest
import org.scalatest.wordspec.AnyWordSpec
import Controller.Controller
import _root_.Controller.GameBuilder.GameBuilder
import _root_.Controller.COR.CORImpl.{LifePointsHandler, PotentialSchwimmerHandler, SchwimmerHandler}
import Model.BaseImpl.{Card, CardDeck, GameState, User}
import org.scalatest.wordspec.AnyWordSpec

class SchwimmerHandlerTest extends AnyWordSpec {

  "SchwimmerHandler" should {

    "detect swimmers and add a swimmer life" in {
      val potentialSchwimmer = User(Seq(Card("Herz", "7"), Card("Pik", "10"), Card("Karo", "K")), 0, "PotentialSchwimmer")
      val table = User(Seq.empty, -1, "Table")
      val initialGameState = GameState(
        players = Seq(potentialSchwimmer),
        table = table,
        deck = new CardDeck().shuffleDeck()
      )
      val controller = Controller(new GameBuilder {
        override def returnGameState(): GameState = initialGameState
        override def updateTable(): User = table
        override def createCardDeck(): CardDeck = new CardDeck().shuffleDeck()
      })

      val Schwimmer = SchwimmerHandler().handle(controller, Seq(potentialSchwimmer))
      val updatedPotentialSchwimmer = controller.gameState.players.find(_.name == "PotentialSchwimmer").get

      assert(updatedPotentialSchwimmer.lifePoints == 1, "Der Schwimmer sollte ein extra Leben erhalten.")
      assert(updatedPotentialSchwimmer.swimming, "Der Schwimmer sollte als solcher markiert werden.")
      assert(controller.gameState.schwimmer, "Im Spiel soll es jetzt einen Schwimmer geben")
    }
  }
}
