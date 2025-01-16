package ControllerTest
import org.scalatest.wordspec.AnyWordSpec
import Controller.Controller
import _root_.Controller.GameBuilder.GameBuilder
import Model.BaseImpl.{Card, CardDeck, GameState, User}

class ControllerClassTest extends AnyWordSpec {

  val player1 = User(Seq(Card("Herz", "7"), Card("Pik", "10"), Card("Karo", "K")), 1, "Player1")
  val player2 = User(Seq(Card("Kreuz", "7"), Card("Herz", "10"), Card("Herz", "K")), 3, "Player2")
  val table = User(Seq.empty, -1, "Table")
  val gameState = GameState(
    players = Seq(player1, player2),
    table = table,
    deck = new CardDeck().shuffleDeck(),
    queue = 0,
    knockCounter = 0,
    gameOver = false,
    indexCardTable = 0,
    indexCardPlayer = 0
  )
  val controller = Controller(new GameBuilder {
    override def returnGameState(): GameState = gameState
    override def updateTable(): User = table
    override def createCardDeck(): CardDeck = new CardDeck().shuffleDeck()
  })

  "Controller" should {

    "check for Schnauz" in {

    }

    "check if game is over" in {

    }

    "create a new Game" in {

    }

    "call knock" in {

    }

    "call skip" in {

    }

    "call trade All" in {

    }

    "call trade One" in {

    }

    "undo" in {

    }

    "redo" in {

    }

    "reset round" in {

    }
  }
}
