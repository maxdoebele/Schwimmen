package ControllerTest

import Controller._
import Model._
import org.scalatest.wordspec.AnyWordSpec
class GameLogicTest extends AnyWordSpec {

  "Game Logic" should {

    "distribute cards to user" in {
      val initialDeck = CardDeck(List(
        Card("Herz", "7"), Card("Pik", "10"), Card("Kreuz", "King"),
        Card("Karo", "Ace"), Card("Herz", "2"), Card("Pik", "3")
      ))
      val user = User(List.empty, 3, "Player1")
      val (updatedDeck, updatedUser) = GameLogic.distributeCardsToUser(initialDeck, user)
      assert(updatedUser.handDeck.length == 3, "Der Benutzer sollte genau 3 Karten erhalten.")
      assert(updatedDeck.cardDeck.length == initialDeck.cardDeck.length - 3, "Das Deck sollte um 3 Karten reduziert werden.")
      assert(updatedUser.handDeck.forall(initialDeck.cardDeck.contains), "Die Karten des Benutzers sollten aus dem Deck stammen.")
    }

    "allow user to knock" in {
      val gameState = GameState(players = List.empty, table = User(List.empty, -1, "Table"), deck = CardDeck(List.empty))

      val newState = GameLogic.knock(gameState)
      assert(newState.knockCounter == 1, "Der Knock-Zähler sollte um 1 erhöht werden.")
      assert(!newState.gameOver, "Das Spiel sollte noch nicht vorbei sein.")

      val finalState = GameLogic.knock(newState)
      assert(finalState.knockCounter == 2, "Der Knock-Zähler sollte auf 2 erhöht werden.")
      assert(finalState.gameOver, "Das Spiel sollte vorbei sein, wenn zweimal geklopft wurde.")
    }

    "allow user to trade" in {

    }

    "calculate points of player" in {

    }

    "check for schnauz of player" in {

    }
  }

}
