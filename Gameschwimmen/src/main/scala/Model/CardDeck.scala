package Model

import scala.util.Random


case class CardDeck(cardDeck: Seq[Card]) {

    def this() = this(
      cardDeck = for {
        suit <- Seq("Herz", "Pik", "Karo", "Kreuz")
        rank <- Seq("7", "8", "9", "10", "J", "Q", "K", "A")
      } yield Card(suit, rank)
    )

    def shuffleDeck(): CardDeck = {
      copy(cardDeck = Random.shuffle(cardDeck))
    }

    def remove3Cards(): (Seq[Card], CardDeck) = {
      if (cardDeck.length < 3) {
        throw new IllegalStateException("Not enough cards in the deck to remove 3.")
      }
      val (onHoldCard, remaining) = cardDeck.splitAt(3)
      (onHoldCard, copy(cardDeck = remaining))
    }

    def add3Cards(threeCards: Seq[Card]): CardDeck = {
      if (threeCards.length == 3) {
        copy(cardDeck = cardDeck ++ threeCards)
      } else {
        throw new IllegalArgumentException("Exactly 3 cards are required")
      }
    }
}
