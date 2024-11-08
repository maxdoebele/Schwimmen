import Model.Card
import org.scalatest.wordspec.AnyWordSpec

class CardTest extends AnyWordSpec {

  "A Model.Card" should {

    "return a correct visual representation for a Herz card" in {
      val card = new Card("Herz", "A")
      val expectedOutput = List(
        "+-------+",
        "|     A |",
        "|   ♥   |",
        "| A     |",
        "+-------+"
      )
      assert(card.drawCard() == expectedOutput)
    }

    "return a correct visual representation for a Pik card" in {
      val card = new Card("Pik", "10")
      val expectedOutput = List(
        "+-------+",
        "|     10|",
        "|   ♠   |",
        "| 10    |",
        "+-------+"
      )
      assert(card.drawCard() == expectedOutput)
    }

    "return a correct visual representation for an unknown suit" in {
      val card = new Card("Unbekannt", "5")
      val expectedOutput = List(
        "+-------+",
        "|     5 |",
        "|   ?   |",
        "| 5     |",
        "+-------+"
      )
      assert(card.drawCard() == expectedOutput)
    }
  }
}
