package ModelTest

import Model.{Card, User}
import org.scalatest.wordspec.AnyWordSpec

class UserTest extends AnyWordSpec {

  "A User" should {

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

    "lose one live point when losing live points" in {
      val user = User(Seq(Card("Herz", "7")), 3, "Max")
      val updatedUser = user.loseLivePoints()
      assert(updatedUser.livePoints == 2)
    }

    // ---------------- live points ab -1 bzw. 0 ist der user raus -------------------

  }
}
