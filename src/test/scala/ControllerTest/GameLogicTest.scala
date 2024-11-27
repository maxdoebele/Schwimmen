package ControllerTest

import Controller._
import Model._
import org.scalatest.wordspec.AnyWordSpec
class GameLogicTest extends AnyWordSpec {

  "Game Logic" should {

    "distribute cards to user" in {
      val initialDeck = new CardDeck().shuffleDeck()
      val user = User(Seq.empty, livePoints = 3, name = "Player1")

      val (updatedDeck, updatedUser) = GameLogic.distributeCardsToUser(initialDeck, user)

      assert(updatedUser.handDeck.length == 3, "Der Benutzer sollte genau 3 Karten erhalten.")
      assert(updatedDeck.cardDeck.length == initialDeck.cardDeck.length - 3, "Das Deck sollte um 3 Karten reduziert werden.")
      assert(updatedUser.handDeck.forall(card => initialDeck.cardDeck.contains(card)), "Die Karten des Benutzers sollten aus dem ursprünglichen Deck stammen.")
    }

    "allow user to knock" in {
      val gameState = GameState(
        players = Seq(User(Seq.empty, 3, "Player1")),
        table = User(Seq.empty, 0, "Table"),
        deck = new CardDeck().shuffleDeck()
      )

      val newState = GameLogic.knock(gameState)
      assert(newState.knockCounter == 1, "Der Knock-Zähler sollte um 1 erhöht werden.")
      assert(!newState.gameOver, "Das Spiel sollte noch nicht vorbei sein.")

      val finalState = GameLogic.knock(newState)

      assert(finalState.knockCounter == 2, "Der Knock-Zähler sollte auf 2 erhöht werden.")
      assert(finalState.gameOver, "Das Spiel sollte vorbei sein, wenn zweimal geklopft wurde.")
    }

    "allow user to trade" in {
      val tableDeck = User(Seq(Card("Herz", "7"), Card("Pik", "10"), Card("Karo", "K")), -1, "Table")
      val player = User(Seq(Card("Kreuz", "A"), Card("Herz", "8"), Card("Pik", "J")), 3, "Player1")
      val gameState = GameState(
        players = Seq(player),
        table = tableDeck,
        deck = new CardDeck().shuffleDeck()
      )

      val newState = GameLogic.tradeOneCard(gameState, 1, 2, player)

      val updatedPlayer = newState.players.find(_.name == player.name).get
      assert(updatedPlayer.handDeck.contains(Card("Karo", "K")), "Der Spieler sollte die Karte aus der Mitte erhalten.")
      assert(!newState.table.handDeck.contains(Card("Karo","K")), "Der Tisch sollte die getauschte Karte nicht mehr im deck haben.")
      assert(!updatedPlayer.handDeck.contains(Card("Herz", "8")), "Die getauschte Karte sollte nicht mehr im Deck des Spielers sein.")
      assert(newState.table.handDeck.contains(Card("Herz", "8")), "Die getauschte Karte sollte in die Mitte gelegt worden sein.")
    }

    "calculate points of player" in {
      val sameSuitCards = Seq(Card("Herz", "7"), Card("Herz", "10"), Card("Herz", "A"))
      val mixedSuitCards = Seq(Card("Herz", "7"), Card("Pik", "10"), Card("Karo", "K"))
      val sameRankCards = Seq(Card("Herz", "K"), Card("Pik", "K"), Card("Karo", "K"))

      assert(GameLogic.calculatePoints(sameSuitCards) == 27, "Punkte sollten die Summe derselben Farbe sein.")
      assert(GameLogic.calculatePoints(mixedSuitCards) == 10, "Punkte sollten der höchste Rang sein, wenn alle Farben unterschiedlich sind.")
      assert(GameLogic.calculatePoints(sameRankCards) == 30.5, "Drei Karten desselben Rangs sollten 30.5 Punkte geben.")
    }

    "check for schnauz of player" in {
      val player1 = User(Seq(Card("Herz", "10"), Card("Herz", "J"), Card("Herz", "A")), 3, "Player1") // 31 Punkte
      val player2 = User(Seq(Card("Kreuz", "K"), Card("Pik", "K"), Card("Karo", "K")), 3, "Player2") // 30.5 Punkte
      val gameState = GameState(
        players = Seq(player1, player2),
        table = User(Seq.empty, -1, "Table"),
        deck = new CardDeck().shuffleDeck()
      )

      val newState = GameLogic.checkForSchnauz(gameState)

      assert(newState.gameOver, "Das Spiel sollte beendet sein, wenn ein Spieler Schnauz hat.")
      assert(newState.players.contains(player1), "Spieler1 sollte als Gewinner mit 31 Punkten erkannt werden.")
    }
  }

}
