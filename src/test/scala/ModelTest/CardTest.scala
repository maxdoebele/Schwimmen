package ModelTest

import Model.BaseImpl.Card
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.Json

import scala.xml.{Elem, Utility}

class CardTest extends AnyWordSpec {

  "A Card" should {

    "return the correct points for '7'" in {
      val card = Card("Herz", "7")
      assert(card.rankToPoints == 7)
    }

    "return the correct points for '8'" in {
      val card = Card("Pik", "8")
      assert(card.rankToPoints == 8)
    }

    "return the correct points for '9'" in {
      val card = Card("Karo", "9")
      assert(card.rankToPoints == 9)
    }

    "return the correct points for '10'" in {
      val card = Card("Kreuz", "10")
      assert(card.rankToPoints == 10)
    }

    "return the correct points for 'J'" in {
      val card = Card("Herz", "J")
      assert(card.rankToPoints == 10)
    }

    "return the correct points for 'Q'" in {
      val card = Card("Pik", "Q")
      assert(card.rankToPoints == 10)
    }

    "return the correct points for 'K'" in {
      val card = Card("Karo", "K")
      assert(card.rankToPoints == 10)
    }

    "return the correct points for 'A'" in {
      val card = Card("Kreuz", "A")
      assert(card.rankToPoints == 11)
    }

    "return 0 points for an invalid rank" in {
      val card = Card("Herz", "Z")
      assert(card.rankToPoints == 0)
    }

    "serialize to XML correctly" in {
      val card = Card("Herz", "7")
      val expectedXML = <Card>
        <suit>Herz</suit>
        <rank>7</rank>
      </Card>
      assert(Utility.trim(card.toXML) == Utility.trim(expectedXML), "Die XML-Darstellung der Karte sollte korrekt sein.")
    }

    "serialize to JSON correctly" in {
      val card = Card("Herz", "7")
      val expectedJSON = "{\"suit\":\"Herz\",\"rank\":\"7\"}"
      assert(card.toJSON.toString() == expectedJSON, "Die JSON-Darstellung der Karte sollte korrekt sein.")
    }

    "deserialize from XML correctly" in {
      val cardXML = <Card>
        <suit>Herz</suit>
        <rank>7</rank>
      </Card>
      val card = Card.fromXML(cardXML)
      assert(card == Card("Herz", "7"), "Die Karte sollte korrekt aus XML deserialisiert werden.")
    }

    "deserialize from JSON correctly" in {
      val cardJSON = "{\"suit\":\"Herz\",\"rank\":\"7\"}"
      val card = Json.parse(cardJSON).as[Card]
      assert(card == Card("Herz", "7"), "Die Karte sollte korrekt aus JSON deserialisiert werden.")
    }
  }
}

