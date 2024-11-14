package Controller
import Model._

class CardOperation(val suit: String, val rank: String) {

  def countPoints(handDeck: Seq[Card]): Int = {
    val sameSuitCards = handDeck.filter(_.suit == this.suit)
    sameSuitCards.map(_.rankToPoints).sum
  }
}
