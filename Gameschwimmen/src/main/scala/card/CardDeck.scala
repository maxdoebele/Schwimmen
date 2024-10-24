import javax.smartcardio.Card

class CardDeck() {
  // Mögliche Farben (Suits)
  val suits = Seq("Herz", "Pik", "Karo", "Kreuz")

  // Mögliche Werte (Ranks)
  val ranks = Seq("7", "8", "9", "10", "J", "Q", "K", "A")

  // Eine Liste, die alle möglichen Karten kombiniert (Deck)
  val cardDeck: Seq[Card] = for {
    suit <- suits
    rank <- ranks
  } yield new Card(suit, rank)

  // Methode, um das aktuelle Deck anzuzeigen
  def showDeck(): Unit = {
    cardDeck.foreach(card => println(s"${card.rank} ${card.suit}"))
  }
}
def shuffleDeck(cardDeck: Seq[Card]): Seq[Card] = {
  val shuffeledDeck = scala.util.Random.shuffle(cardDeck)
  shuffeledDeck
}

