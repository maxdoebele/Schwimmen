import Model.CardDeck
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import java.io.ByteArrayOutputStream
import java.io.PrintStream

class CardDeckTest extends AnyWordSpec {

  "A Model.CardDeck" should {

    "contain 32 cards when initialized" in {
      val deck = new CardDeck
      assert(deck.cardDeck.size == 32, "Deck sollte 32 Karten enthalten")
    }

    "have all cards with valid suits and ranks" in {
      val deck = new CardDeck
      val validSuits = Seq("Herz", "Pik", "Karo", "Kreuz")
      val validRanks = Seq("7", "8", "9", "10", "J", "Q", "K", "A")

      assert(deck.cardDeck.forall(card => validSuits.contains(card.suit) && validRanks.contains(card.rank)))
    }

    "shuffle the cards, changing the order" in {
      val deck = new CardDeck
      val originalOrder = deck.cardDeck
      deck.shuffleDeck()
      assert(deck.cardDeck != originalOrder, "Die Reihenfolge des Decks sollte nach dem Mischen anders sein")
    }

    "still contain 32 cards after shuffling" in {
      val deck = new CardDeck
      deck.shuffleDeck()
      assert(deck.cardDeck.size == 32, "Deck sollte nach dem Mischen immer noch 32 Karten enthalten")
    }

    // Hilfsmethode zum Abfangen der Ausgabe
    def captureOutput(method: => Unit): String = {
      val outputStream = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(outputStream)) {
        method // Die Methode aufrufen, deren Ausgabe wir abfangen wollen
      }
      outputStream.toString
    }

    "correctly show all cards in the deck" in {
      val deck = new CardDeck
      val expectedOutput = deck.cardDeck.map(card => s"${card.rank} ${card.suit}").mkString("\n") + "\n"

      val output = captureOutput {
        deck.showDeck()
      }

      assert(output == expectedOutput, "Die Ausgabe von showDeck sollte alle Karten korrekt anzeigen")
    }
  }
}
