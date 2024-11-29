package View

import Controller._
import Model._

import scala.io.StdIn.readLine

class TUI {
  def tuiActionHandler(currentPlayer: User, gameState: GameState): GameState = {

    println(s"${currentPlayer.name}, Du bist dran! Wähle eine Aktion: 1 = Klopfen, 2 = Schieben, 3 = Tauschen")
    readLine("Gib eine Nummer ein: ") match {
      case "1" =>
        knockAction.playerActions(currentPlayer, gameState, -1, -1)
      case "2" =>
        skipAction.playerActions(currentPlayer, gameState, -1 , -1)
      case "3" =>
        println("1: Alle Karten tauschen, 2: Eine Karte tauschen")
        readLine("Gib eine Nummer ein: ") match {
          case "1" =>
            val afterTradeGameState = tradeAllAction.playerActions(currentPlayer, gameState, -1, -1)
            displayGameState(afterTradeGameState)
            afterTradeGameState
          case "2" =>
            println("0: erste Karte, 1: mittlere Karte, 2: letzte Karte")
            val input = readLine("Gib einmal die Zahl für dein Deck und einmal die Zahl für das Tischdeck ein (0 0), (0 1) etc: ")
            val indices = input.split(" ").map(_.toInt)
            val afterTradeOneGameState = tradeOneAction.playerActions(currentPlayer, gameState, indices(0), indices(1))
            displayGameState(afterTradeOneGameState)
            afterTradeOneGameState
          case _ =>
            println("Falsche Eingabe, versuche es erneut.")
            tuiActionHandler(currentPlayer, gameState)
        }
      case _ =>
        println("Falsche Eingabe, versuche es erneut.")
        tuiActionHandler(currentPlayer, gameState)
    }
  }

  def toNumber(input: String): Int = {
    try {
      input.toInt
    } catch {
      case _: NumberFormatException => -1
    }
  }

  def drawCard(card: Card): List[String] = {
    val suitSymbol = card.suit match {
      case "Herz"  => "♥"
      case "Pik"   => "♠"
      case "Karo"  => "♦"
      case "Kreuz" => "♣"
      case _       => "?"
    }

    List(
      "+-------+",
      f"|     ${card.rank}%-2s|",
      f"|   $suitSymbol   |",
      f"| ${card.rank}%-2s    |",
      "+-------+"
    )
  }

  def displayGameState(gameState: GameState): Unit = {
    gameState.players.foreach { player =>
      println(s"${player.name}'s Karten:")
      displayHand(player.handDeck)
      println()
    }

    println("Tisch Karten:")
    displayHand(gameState.table.handDeck)
    println() // Line break after table hand
  }

  private def displayHand(handDeck: Seq[Card]): Unit = {
    val drawnCards = handDeck.map(drawCard)
    val transposedLines = drawnCards.transpose
    transposedLines.foreach(row => println(row.mkString(" ")))
  }
}
