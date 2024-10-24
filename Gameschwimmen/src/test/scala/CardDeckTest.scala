import org.scalatest.wordspec.AnyWordSpec

class CardDeckTest extends AnyWordSpec {

  "A CardDeck" should {

    "contain 32 cards when initialized" in {
      val deck = new CardDeck
      assert(deck.cardDeck.size == 32, "Deck sollte 32 Karten enthalten")
    }

    "have all cards with valid suits and ranks" in {
      val deck = new CardDeck
      val validSuits = Seq("Herz", "Pik", "Karo", "Kreuz")
      val validRanks = Seq("7", "8", "9", "10", "J", "Q", "K", "A")

      assert(deck.cardDeck.forall(card => validSuits.contains(card.suit) && validRanks.contains(card.rank)))
    }

    "shuffle the cards, changing the order" in {
      val deck = new CardDeck
      val originalOrder = deck.cardDeck
      deck.shuffleDeck()
      assert(deck.cardDeck != originalOrder, "Die Reihenfolge des Decks sollte nach dem Mischen anders sein")
    }

    "still contain 32 cards after shuffling" in {
      val deck = new CardDeck
      deck.shuffleDeck()
      assert(deck.cardDeck.size == 32, "Deck sollte nach dem Mischen immer noch 32 Karten enthalten")
    }
  }
}
