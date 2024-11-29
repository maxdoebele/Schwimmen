package Model

case class User(handDeck: Seq[Card], livePoints: Int, name: String) {
  def removeCard(index: Int): Option[(User, Card)] = {
    if (index >= 0 && index < handDeck.size) {
      val cardToRemove = handDeck(index)
      val updatedHandDeck = handDeck.patch(index, Nil, 1)
      Some((copy(handDeck = updatedHandDeck), cardToRemove))
    } else {
      None
    }
  }

  def addCard(card: Card): User = {
    val updatedHandDeck = handDeck :+ card
    copy(handDeck = updatedHandDeck)
  }
  def add3Cards(cards: Seq[Card]): User = {
    val updatedHandDeck = handDeck ++ cards
    copy(handDeck = updatedHandDeck)
  }

  def removeAllCards(): (User, Seq[Card]) = {
    val removedCards = handDeck
    (copy(handDeck = Seq.empty), removedCards)
  }

  def loseLivePoint(): User = {
    val lostLivePoints = livePoints - 1
    copy(livePoints = lostLivePoints)
  }

}
