package Model.BaseImpl

import scala.xml.Elem

case class Card(val suit: String, val rank: String, var isSelected: Boolean = false) {
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
  
  def toXML: Elem = <Card>
    <suit>
      {this.suit}
    </suit>
    <rank>
      {this.rank}
    </rank>
  </Card>
  
}

object Card {
  def fromXML(node: scala.xml.Node): Card = {
    val suit = (node \ "suit").text
    val rank = (node \ "rank").text
    Card(suit = suit, rank = rank) // Create a new instance of Card
  }
}
