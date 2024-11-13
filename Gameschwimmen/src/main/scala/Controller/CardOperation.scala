package Controller

class Card(val suit: String, val rank: String) {
  def rankToPoints: Int = {
    rank match {
      case "7" => 7
      case "8" => 8
      case "9" => 9
      case "10" => 10
      case "J" => 10
      case "Q" => 10
      case "K" => 10
      case "A" => 11
      case _ => 0
    }
  }

  def countPoints(cards: Seq[Card]): Int = {
    val sameSuitCards = cards.filter(_.suit == this.suit)
    sameSuitCards.map(_.rankToPoints).sum
  }
}
