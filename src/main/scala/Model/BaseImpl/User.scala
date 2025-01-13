package Model.BaseImpl

import play.api.libs.json.{Json, OFormat}

import scala.xml.Elem

case class User(handDeck: Seq[Card], lifePoints: Int, name: String, swimming: Boolean = false) {
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
    this.copy(swimming = true)
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
    val lostLifePoints = lifePoints - 1
    copy(lifePoints = lostLifePoints)
  }

  def addLifePoint(): User = {
    val addedLifePoints = lifePoints + 1
    copy(lifePoints = addedLifePoints)
  }

  def toXML: Elem = <player>
    <handDeck>
      {this.handDeck.map(card => card.toXML)}
    </handDeck>
    <lifePoints>{this.lifePoints}</lifePoints>
    <name>{this.name}</name>
    <swimming>{this.swimming}</swimming>
  </player>
}

object User {
  def fromXML(node: scala.xml.Node): User = {
    val handDeck = (node \ "handDeck" \ "Card").map(cardNode => Card.fromXML(cardNode))
    val lifePoints = (node \ "lifePoints").text.toInt
      println(lifePoints)
    val name = (node \ "name").text.trim
    val swimming = (node \ "swimming").text.toBoolean

    User(handDeck = handDeck, lifePoints = lifePoints, name = name, swimming = swimming)
  }

  implicit val cardFormat: OFormat[User] = Json.format[User]
}
