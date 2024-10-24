class CardDeck() {
  // Mögliche Farben (Suits)
  val suits = Seq("Herz", "Pik", "Karo", "Kreuz")

  // Mögliche Werte (Ranks)
  val ranks = Seq("7", "8", "9", "10", "J", "Q", "K", "A")

  // Eine Liste, die alle möglichen Karten kombiniert (Deck)
  var cardDeck: Seq[Card] = for {
    suit <- suits
    rank <- ranks
  } yield new Card(suit, rank)

  // Methode zum Mischen des Decks
  def shuffleDeck(): Seq[Card] = {
    cardDeck = scala.util.Random.shuffle(cardDeck)
    cardDeck
  }

  // Methode, um das aktuelle Deck anzuzeigen
  def showDeck(): Unit = {
    cardDeck.foreach(card => println(s"${card.rank} ${card.suit}"))
  }
}
