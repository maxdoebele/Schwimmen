package ModelTest

import Model.BaseImpl.CardDeck
import org.scalatest.wordspec.AnyWordSpec

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
  }
}
