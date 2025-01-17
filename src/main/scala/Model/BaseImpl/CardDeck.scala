package Model.BaseImpl

import play.api.libs.json.{Json, OFormat}

import scala.util.Random
import scala.xml.Elem

case class CardDeck(cardDeck: Seq[Card]) {

  def this() = this(
    cardDeck = for {
      suit <- Seq("Herz", "Pik", "Caro", "Kreuz")
      rank <- Seq("7", "8", "9", "10", "J", "Q", "K", "A")
    } yield Card(suit, rank)
  )

  def shuffleDeck(): CardDeck = {
    copy(cardDeck = Random.shuffle(cardDeck))
  }

  def remove3Cards(): (Seq[Card], CardDeck) = {
    val (onHoldCard, remaining) = cardDeck.splitAt(3)
    (onHoldCard, copy(cardDeck = remaining))
  }

  def add3Cards(threeCards: Seq[Card]): CardDeck = {
    copy(cardDeck = cardDeck ++ threeCards)

  }

  def toXML(): Elem = <CardDeck>
    {this.cardDeck.map(card => card.toXML)}
  </CardDeck>
}

object CardDeck {
  def fromXML(node: scala.xml.Node): CardDeck = {
    val cardDeck = (node \ "CardDeck" \ "Card").map(cardNode => Card.fromXML(cardNode))
    CardDeck(cardDeck)
  }

  implicit val cardDeckFormat: OFormat[CardDeck] = Json.format[CardDeck]
}
