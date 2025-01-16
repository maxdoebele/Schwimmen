package ModelTest

import Model.BaseImpl.{Card, User}
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.Json

class UserTest extends AnyWordSpec {

  "A User" should {
    
    "set swimmer" in {
      val user = User(Seq(Card("Herz", "7")), 1, "Max")
      val updatedUser = user.setSchwimmer()
      assert(updatedUser.swimming)
    }

    "remove a card from the hand" in {
      val user = User(Seq(Card("Herz", "7"), Card("Pik", "8"), Card("Karo", "9")), 3, "Max")
      val (updatedUser, removedCard) = user.removeCard(1).get
      assert(updatedUser.handDeck.length == 2)
      assert(removedCard == Card("Pik", "8"))
      assert(updatedUser.handDeck.contains(Card("Herz", "7")))
      assert(updatedUser.handDeck.contains(Card("Karo", "9")))
    }

    "return None when removing a card with an invalid index" in {
      val user = User(Seq(Card("Herz", "7"), Card("Pik", "8")), 3, "Max")
      val result = user.removeCard(5)
      assert(result.isEmpty)
    }

    "add a single card to the hand" in {
      val user = User(Seq(Card("Herz", "7"), Card("Pik", "8")), 3, "Max")
      val updatedUser = user.addCard(Card("Kreuz", "10"))
      assert(updatedUser.handDeck.length == 3)
      assert(updatedUser.handDeck.contains(Card("Kreuz", "10")))
    }

    "add three cards to the hand" in {
      val user = User(Seq(Card("Herz", "7")), 3, "Max")
      val updatedUser = user.add3Cards(Seq(Card("Pik", "8"), Card("Karo", "9"), Card("Kreuz", "10")))
      assert(updatedUser.handDeck.length == 4)
      assert(updatedUser.handDeck.contains(Card("Pik", "8")))
      assert(updatedUser.handDeck.contains(Card("Karo", "9")))
      assert(updatedUser.handDeck.contains(Card("Kreuz", "10")))
    }
    
    "lose lifepoint" in {
      val user = User(Seq(Card("Herz", "7")), 3, "Max")
      val updatedUser = user.loseLifePoint()
      assert(updatedUser.lifePoints == 2)
    }
    
    "add lifepoint" in {
      val user = User(Seq(Card("Herz", "7")), 0, "Max")
      val updatedUser = user.addLifePoint()
      assert(updatedUser.lifePoints == 1)
    }

    "serialize to XML correctly" in {
      val user = User(Seq(Card("Herz", "7")), 3, "Max")
      val xml = user.toXML
      assert((xml \ "name").text == "Max")
      assert((xml \ "lifePoints").text == "3")
      assert((xml \ "handDeck").nonEmpty)
    }

    "deserialize from XML correctly" in {
      val xml = <player>
        <handDeck>
          <Card>
            <suit>Herz</suit>
            <rank>7</rank>
          </Card>
          <Card>
            <suit>Pik</suit>
            <rank>8</rank>
          </Card>
        </handDeck>
        <lifePoints>3</lifePoints>
        <name>Max</name>
        <swimming>false</swimming>
      </player>
      val user = User.fromXML(xml)
      assert(user.name == "Max")
      assert(user.lifePoints == 3)
      assert(user.handDeck.length == 2)
      assert(user.handDeck.contains(Card("Herz", "7")))
      assert(user.handDeck.contains(Card("Pik", "8")))
    }

    "serialize and deserialize to and from JAML correctly" in {
      val user = new User(Seq(Card("Herz", "7")), 3, "Max")
      val json = Json.toJson(user)
      val deckFromJson = json.as[User]
      assert(user == deckFromJson)
    }
  }
}
