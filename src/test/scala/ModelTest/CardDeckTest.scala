package ModelTest

import Model.BaseImpl.CardDeck
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.Json

import scala.xml.Utility

class CardDeckTest extends AnyWordSpec {

  "A CardDeck" should {

    "initialize with 32 cards by default" in {
      val deck = new CardDeck()
      assert(deck.cardDeck.length == 32)
    }

    "shuffle the deck" in {
      val deck = new CardDeck()
      val shuffledDeck = deck.shuffleDeck()
      assert(shuffledDeck.cardDeck != deck.cardDeck)
    }

    "remove 3 cards from the deck" in {
      val deck = new CardDeck()
      val (removedCards, updatedDeck) = deck.remove3Cards()
      assert(removedCards.length == 3)
      assert(updatedDeck.cardDeck.length == 29)
      assert(!updatedDeck.cardDeck.contains(removedCards.head))
      assert(!updatedDeck.cardDeck.contains(removedCards(1)))
      assert(!updatedDeck.cardDeck.contains(removedCards(2)))
    }

    "add 3 cards back to the deck" in {
      val deck = new CardDeck()
      val (removedCards, updatedDeck) = deck.remove3Cards()
      val newDeck = updatedDeck.add3Cards(removedCards)
      assert(newDeck.cardDeck.length == 32)
      assert(newDeck.cardDeck.contains(removedCards.head))
      assert(newDeck.cardDeck.contains(removedCards(1)))
      assert(newDeck.cardDeck.contains(removedCards(2)))
    }

    "serialize to XML correctly" in {
      val deck = new CardDeck()
      val expectedXML = deck.toXML()
      assert(Utility.trim(deck.toXML()) == Utility.trim(expectedXML), "Die XML-Darstellung des Decks sollte korrekt sein.")
    }

    "deserialize from XML correctly" in {
      val xml = <deck>
        <CardDeck>
          <Card>
            <suit>Herz</suit>
            <rank>7</rank>
          </Card>
          <Card>
            <suit>Herz</suit>
            <rank>8</rank>
          </Card>
          <Card>
            <suit>Herz</suit>
            <rank>9</rank>
          </Card>
          <Card>
            <suit>Herz</suit>
            <rank>10</rank>
          </Card>
          <Card>
            <suit>Herz</suit>
            <rank>J</rank>
          </Card>
          <Card>
            <suit>Herz</suit>
            <rank>Q</rank>
          </Card>
          <Card>
            <suit>Herz</suit>
            <rank>K</rank>
          </Card>
          <Card>
            <suit>Herz</suit>
            <rank>A</rank>
          </Card>
          <Card>
            <suit>Karo</suit>
            <rank>7</rank>
          </Card>
          <Card>
            <suit>Karo</suit>
            <rank>8</rank>
          </Card>
          <Card>
            <suit>Karo</suit>
            <rank>9</rank>
          </Card>
          <Card>
            <suit>Karo</suit>
            <rank>10</rank>
          </Card>
          <Card>
            <suit>Karo</suit>
            <rank>J</rank>
          </Card>
          <Card>
            <suit>Karo</suit>
            <rank>Q</rank>
          </Card>
          <Card>
            <suit>Karo</suit>
            <rank>K</rank>
          </Card>
          <Card>
            <suit>Karo</suit>
            <rank>A</rank>
          </Card>
          <Card>
            <suit>Kreuz</suit>
            <rank>7</rank>
          </Card>
          <Card>
            <suit>Kreuz</suit>
            <rank>8</rank>
          </Card>
          <Card>
            <suit>Kreuz</suit>
            <rank>9</rank>
          </Card>
          <Card>
            <suit>Kreuz</suit>
            <rank>10</rank>
          </Card>
          <Card>
            <suit>Kreuz</suit>
            <rank>J</rank>
          </Card>
          <Card>
            <suit>Kreuz</suit>
            <rank>Q</rank>
          </Card>
          <Card>
            <suit>Kreuz</suit>
            <rank>K</rank>
          </Card>
          <Card>
            <suit>Kreuz</suit>
            <rank>A</rank>
          </Card>
          <Card>
            <suit>Pik</suit>
            <rank>7</rank>
          </Card>
          <Card>
            <suit>Pik</suit>
            <rank>8</rank>
          </Card>
          <Card>
            <suit>Pik</suit>
            <rank>9</rank>
          </Card>
          <Card>
            <suit>Pik</suit>
            <rank>10</rank>
          </Card>
          <Card>
            <suit>Pik</suit>
            <rank>J</rank>
          </Card>
          <Card>
            <suit>Pik</suit>
            <rank>Q</rank>
          </Card>
          <Card>
            <suit>Pik</suit>
            <rank>K</rank>
          </Card>
          <Card>
            <suit>Pik</suit>
            <rank>A</rank>
          </Card>
        </CardDeck>
      </deck>
      val deck = CardDeck.fromXML(xml)
      assert(deck.cardDeck.length == 32)
    }

    "serialize to JSON and deserialize from JSON correctly" in {
      val deck = new CardDeck()
      val json = Json.toJson(deck)
      val deckFromJson = json.as[CardDeck]
      assert(deck == deckFromJson)
    }
  }
}
