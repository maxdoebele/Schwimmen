package Model.BaseImpl

import Model.BaseImpl.Card

case class User(handDeck: Seq[Card], lifePoints: Int, name: String, schwimmt: Boolean = false) {
  def removeCard(index: Int): Option[(User, Card)] = {
    if (index >= 0 && index < handDeck.size) {
      val cardToRemove = handDeck(index)
      val updatedHandDeck = handDeck.patch(index, Nil, 1)
      Some((copy(handDeck = updatedHandDeck), cardToRemove))
    } else {
      None
    }
  }

  def setSchwimmer(): User = {
    this.copy(schwimmt = true)
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

  def loseLifePoint(): User = {
    val lostLivePoints = lifePoints - 1
    copy(lifePoints = lostLivePoints)
  }

  def addLifePoint(): User = {
    val addedLivePoints = lifePoints + 1
    copy(lifePoints = addedLivePoints)
  }
}
