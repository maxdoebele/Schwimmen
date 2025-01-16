package ModelTest

import Model.BaseImpl.{Card, CardDeck, GameState, User}
import org.scalatest.wordspec.AnyWordSpec

import scala.xml.XML

class GameStateTest extends AnyWordSpec {

  val player1 = User(Seq(Card("Herz", "7"), Card("Pik", "10"), Card("Karo", "K")), 3, "Max")
  val player2 = User(Seq(Card("Kreuz", "7"), Card("Herz", "10"), Card("Herz", "K")), 3, "Emilia")
  val table = User(Seq(Card("Herz", "A"), Card("Karo", "J"), Card("Herz", "D")), -1, "Table")
  val deck = new CardDeck()

  val gameState = GameState(Seq(player1, player2), table, deck)

  "A GameState" should {

    "initialize correctly with the given players, table, and deck" in {
      assert(gameState.players.size == 2)
      assert(gameState.players.contains(player1))
      assert(gameState.players.contains(player2))
      assert(gameState.table == table)
      assert(gameState.deck == deck)
    }

    "initialize with default values for round, knockCounter, and gameOver" in {
      assert(gameState.queue == 0)
      assert(gameState.knockCounter == 0)
      assert(!gameState.gameOver)
    }

    "update the round number correctly when incremented" in {
      assert(gameState.queue == 0)

      val updatedGameState = gameState.copy(queue = gameState.queue + 1)
      assert(updatedGameState.queue == 1)
    }

    "correctly handle the gameOver state" in {
      assert(!gameState.gameOver)

      val updatedGameState = gameState.copy(gameOver = true)
      assert(updatedGameState.gameOver)
    }

    "handle knockCounter correct" in {
      assert(gameState.knockCounter == 0)

      val updatedGameState = gameState.copy(knockCounter = gameState.knockCounter + 1)
      assert(updatedGameState.knockCounter == 1)
    }

    "serialize to XML correctly" in {
      val xml = gameState.toXML

      assert((xml \ "players").nonEmpty)
      assert((xml \ "table").nonEmpty)
      assert((xml \ "deck").nonEmpty)
      assert((xml \ "queue").text == "0")
      assert((xml \ "knockCounter").text == "0")
      assert((xml \ "gameOver").text == "false")
      assert((xml \ "indexCardPlayer").text == "0")
      assert((xml \ "indexCardTable").text == "0")
      assert((xml \ "swimmerGlobal").text == "false")
      assert((xml \ "roundCounter").text == "0")
      assert((xml \ "lastLooser").nonEmpty)

      val parsedXml = XML.loadString(xml.toString())
      assert(parsedXml.isInstanceOf[scala.xml.Elem])
    }

    "deserialize from XML correctly" in {
      val xml = gameState.toXML
      val deserializedGameState = GameState.fromXML(xml)

      assert(deserializedGameState.players == gameState.players)
      assert(deserializedGameState.table == gameState.table)
      assert(deserializedGameState.deck == gameState.deck)
      assert(deserializedGameState.queue == gameState.queue)
      assert(deserializedGameState.knockCounter == gameState.knockCounter)
      assert(deserializedGameState.gameOver == gameState.gameOver)
      assert(deserializedGameState.indexCardPlayer == gameState.indexCardPlayer)
      assert(deserializedGameState.indexCardTable == gameState.indexCardTable)
      assert(deserializedGameState.schwimmer == gameState.schwimmer)
      assert(deserializedGameState.roundCounter == gameState.roundCounter)
      assert(deserializedGameState.lastLoosers == gameState.lastLoosers)
    }
  }
}

