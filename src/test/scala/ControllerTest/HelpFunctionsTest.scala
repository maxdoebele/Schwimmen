package ControllerTest

import Controller.*
import _root_.Controller.GameBuilder.GameBuilderImpl.BuildNewGame
import FileIO.FileIOImpl.FileIOJSON
import Model.*
import Model.BaseImpl.{Card, CardDeck, GameState, User}
import org.scalatest.wordspec.AnyWordSpec

class HelpFunctionsTest extends AnyWordSpec {

  "Game Logic" should {
    
    "calculate current score" in {
      val player1 = User(Seq(Card("Herz", "7"), Card("Herz", "10"), Card("Herz", "K")), 3, "Player1")
      val player2 = User(Seq(Card("Karo", "7"), Card("Karo", "10"), Card("Karo", "K")), 3, "Player2")
      val player = Seq("Player1", "Player2")
      val controller = Controller(BuildNewGame(player), new FileIOJSON)
      controller.gameState = GameState(
        players = Seq(player1, player2),
        table = User(Seq.empty, -1, "Table"),
        deck = new CardDeck().shuffleDeck()
      )
      val result = HelpFunctions.calculateCurrentScore(controller)
      val expectedScore = Map(
        "Player1" -> 3,
        "Player2" -> 3
      )
      assert(result == expectedScore, "Der aktuelle Punktestand sollte 3 sein.")
    }
    
    "get current player" in {
      val player1 = User(Seq(Card("Herz", "7"), Card("Pik", "10"), Card("Karo", "K")), 3, "Player1")
      val player2 = User(Seq(Card("Kreuz", "7"), Card("Herz", "10"), Card("Pik", "K")), 3, "Player2")
      val gameState = GameState(
        players = Seq(player1, player2),
        table = User(Seq.empty, -1, "Table"),
        deck = new CardDeck().shuffleDeck()
      )
      assert(HelpFunctions.getCurrentPlayer(gameState) == player1, "Der aktuelle Spieler sollte der erste Spieler sein.")
    }

    "calculatePoints" should {

      "correctly calculate points of player" in {
        // Card sets
        val cards1 = Seq(Card("Herz", "7"), Card("Herz", "10"), Card("Herz", "K"))
        val cards2 = Seq(Card("Kreuz", "7"), Card("Pik", "10"), Card("Herz", "K"))

        // Calculating points
        val points1 = HelpFunctions.calculatePoints(cards1)
        val points2 = HelpFunctions.calculatePoints(cards2)

        // Assert points calculation is successful for both cases
        assert(points1.isSuccess, "Die Punkteberechnung für cards1 sollte erfolgreich sein.")
        assert(points1.get == 27, "Die Punkte von cards1 sollte 27 sein.")

        assert(points2.isSuccess, "Die Punkteberechnung für cards2 sollte erfolgreich sein.")
        assert(points2.get == 10, "Die Punkte von cards2 sollte 10 sein.")
      }

      "throw an exception if the number of cards is not exactly 3" in {
        val cards = Seq(Card("Herz", "7"), Card("Herz", "10"))
        val result = HelpFunctions.calculatePoints(cards)

        assert(result.isFailure, "Die Berechnung sollte fehlschlagen, wenn nicht genau 3 Karten übergeben werden.")
        assert(result.failed.get.isInstanceOf[IllegalArgumentException], "Es sollte eine IllegalArgumentException geworfen werden.")
      }

      "calculate points correctly for same suit group" in {
        val cards = Seq(Card("Herz", "7"), Card("Herz", "10"), Card("Herz", "K"))
        val result = HelpFunctions.calculatePoints(cards)

        assert(result.isSuccess, "Die Punkteberechnung für Karten mit gleichem Suit sollte erfolgreich sein.")
        assert(result.get == 27, "Die Punkte von den Karten sollten 27 sein.")
      }

      "calculate points correctly when cards have different suits" in {
        val cards = Seq(Card("Herz", "7"), Card("Kreuz", "10"), Card("Pik", "K"))
        val result = HelpFunctions.calculatePoints(cards)

        assert(result.isSuccess, "Die Punkteberechnung für Karten mit verschiedenen Suits sollte erfolgreich sein.")
        assert(result.get == 10, "Die Punkte sollten 10 sein, da keine zwei Karten den gleichen Suit haben.")
      }
    }

    "find loser of round" in {
      val player1 = User(Seq(Card("Herz", "7"), Card("Kreuz", "10"), Card("Karo", "K")), 3, "Player1")
      val player2 = User(Seq(Card("Pik", "7"), Card("Pik", "10"), Card("Karo", "K")), 3, "Player2")
      val player3 = User(Seq(Card("Pik", "A"), Card("Kreuz", "A"), Card("Karo", "A")), 3, "Player3")
      val playersWithoutFeuer = Seq(player1, player2)
      val playersWithFeuer = Seq(player1, player2, player3)
      val loosersWithoutFeuer = HelpFunctions.findLoserOfRound(playersWithoutFeuer)
      val loosersWithFeuer = HelpFunctions.findLoserOfRound(playersWithFeuer)
      assert(loosersWithoutFeuer.map(_.name) == Seq("Player1"), "Der Spieler mit den wenigsten Punkten sollte verloren haben (player1).")
      assert(loosersWithFeuer.map(_.name) == Seq("Player1","Player2"), "Alle Spieler die kein Feuer haben sollten verloren haben.")
    }
    
    "check for player limit" in {
      val player1 = User(Seq(Card("Herz", "7"), Card("Pik", "8"), Card("Karo", "9")), 3, "Player1")
      val player2 = User(Seq(Card("Herz", "8"), Card("Pik", "9"), Card("Karo", "10")), 3, "Player2")
      val player3 = User(Seq(Card("Herz", "9"), Card("Pik", "10"), Card("Karo", "J")), 3, "Player3")
      val player4 = User(Seq(Card("Herz", "10"), Card("Pik", "J"), Card("Karo", "Q")), 3, "Player4")
      val player5 = User(Seq(Card("Herz", "J"), Card("Pik", "Q"), Card("Karo", "K")), 3, "Player5")
      val player6 = User(Seq(Card("Herz", "Q"), Card("Pik", "K"), Card("Karo", "A")), 3, "Player6")
      val player7 = User(Seq(Card("Herz", "K"), Card("Pik", "A"), Card("Kreuz", "7")), 3, "Player7")
      val player8 = User(Seq(Card("Herz", "A"), Card("Karo", "7"), Card("Kreuz", "8")), 3, "Player8")
      val players1 = Seq("Player1", "Player2", "Player3", "Player4", "Player5", "Player6", "Player7", "Player8")
      val players2 = Seq("Player1")
      val playerLimit1 = HelpFunctions.checkForPlayerLimit(players1)
      val playerLimit2 = HelpFunctions.checkForPlayerLimit(players2)
      assert(playerLimit1, "Die Anzahl der Spieler sollte korrekt zwischen 2 und 9 liegen.")
      assert(!playerLimit2,"Die Anzahl der Spieler sollte zu als wenig erkannt sein")
    }
  }
}
