package Model

case class User(handDeck: Seq[Card], livePoints: Int) {
  def removeCard(card: Card): User = {
    val updatedHand = handDeck.filterNot(_ == card)
    copy(handDeck = updatedHand)
  }

  def addCard(card: Card): User = {
    val updatedHand = handDeck :+ card
    copy(handDeck = updatedHand)
  }

  def updateLivePoints(): User = {
    val updatedLivePoints = livePoints - 1
    copy(livePoints = updatedLivePoints)
  }

}
