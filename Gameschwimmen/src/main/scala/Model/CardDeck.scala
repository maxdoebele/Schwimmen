package Model
import scala.util.Random
case class CardDeck(cardDeck: Seq[Card]) {
  val suits = Seq("Herz", "Pik", "Karo", "Kreuz")
  val ranks = Seq("7", "8", "9", "10", "J", "Q", "K", "A")

  def this() = this(for {
    suit <- suits
    rank <- ranks
  } yield new Card(suit, rank))

  def shuffleDeck(): CardDeck = {
    copy(cardDeck = Random.shuffle(cardDeck))
  }

  def remove3Cards(): (Seq[Card], CardDeck) = {
    if (cardDeck.length >= 3) {
      val (onHoldCard, remaining) = cardDeck.splitAt(3)
      (onHoldCard, copy(cardDeck = remaining))
    } else {
//********************** not defined *****************************
      val newDeck = copy(cardDeck = Seq.empty)
      (cardDeck, newDeck)
    }
  }

  def add3Cards(threeCards: Seq[Card]): CardDeck = {
    if (threeCards.length == 3) {
      copy(cardDeck = cardDeck ++ threeCards)
    } else {
      throw new IllegalArgumentException("Exactly 3 cards are required")
    }
  }

}
