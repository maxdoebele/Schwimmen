package ModelTest

import Model.BaseImpl.Card
import org.scalatest.wordspec.AnyWordSpec

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
  }
}

