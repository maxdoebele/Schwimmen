package Model
import scala.util.Random
case class CardDeck(cardDeck: Seq[Card]) {
  val suits = Seq("Herz", "Pik", "Karo", "Kreuz")
  val ranks = Seq("7", "8", "9", "10", "J", "Q", "K", "A")

  def this() = this(for {
    suit <- suits
    rank <- ranks
  } yield new Card(suit, rank))

  def shuffleDeck(carddeck: Seq[Card]): Seq[Card] = {
    Random.shuffle(carddeck)
  }

  def remove3Cards(): (List[Card], CardDeck) = {
    if (cardDeck.length >= 3) {
      val (onHoldCard, remaining) = cardDeck.splitAt(3)
      (onHoldCard.toList, copy(cardDeck = remaining))
    } else {
      //***************************************************** not defined
      val newDeck = copy(cardDeck = Seq.empty)
      (cardDeck.toList, newDeck)
    }
  }
  def showDeck(): Unit = {
    cardDeck.foreach(card => println(s"${card.rank} ${card.suit}"))
  }
}
