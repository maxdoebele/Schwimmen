package Model

case class User(handDeck: Seq[Card], livePoints: Int, name: String) {
  def removeCard(card: Card): Option[(User, Card)] = {
    handDeck.find(_ == card) match {
      case Some(foundCard) =>
        val updatedHandDeck = handDeck.filterNot(_ == foundCard)
        Some((copy(handDeck = updatedHandDeck), foundCard))
      case None =>
        None
    }
  }

  def addCard(card: Card): User = {
    val updatedHandDeck = handDeck :+ card
    copy(handDeck = updatedHandDeck)
  }
  def addCards(cards: Seq[Card]): User = {
    val updatedHandDeck = handDeck ++ cards
    copy(handDeck = updatedHandDeck)
  }

  def loseLivePoints(): User = {
    val lostLivePoints = livePoints - 1
    copy(livePoints = lostLivePoints)
  }

}
