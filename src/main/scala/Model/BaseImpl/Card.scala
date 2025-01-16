package Model.BaseImpl

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor
import play.api.libs.json.{JsValue, Json, OFormat}

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
    <suit>{this.suit}</suit>
    <rank>{this.rank}</rank>
  </Card>

  def toJSON: JsValue = Json.obj(
    "suit" -> this.suit,
    "rank" -> this.rank
  )
}

object Card {
  def fromXML(node: scala.xml.Node): Card = {
    val suit = (node \ "suit").text.trim
    val rank = (node \ "rank").text.trim
    Card(suit = suit, rank = rank) // Create a new instance of Card
  }

  implicit val cardFormat: OFormat[Card] = Json.format[Card]
}
