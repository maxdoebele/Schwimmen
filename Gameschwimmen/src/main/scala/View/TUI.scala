package View

import Controller.GameLogic
import Model._

import scala.io.StdIn.readLine

class TUI {

  def playerActionHandler(currentPlayer: User, gameState: GameState): GameState = {
    println(s"${currentPlayer.name}, Du bist dran! Wähle eine Aktion: 1 = Klopfen, 2 = Schieben, 3 = Tauschen")
    readLine("Gib eine Nummer ein: ") match {
      case "1" =>
        val afterKnockGameState = GameLogic.knock(gameState)
        println(s"Spieler ${currentPlayer.name} hat geklopft")
        return afterKnockGameState
      case "2" =>
        //skip()
        println(s"Spieler ${currentPlayer.name} hat geschoben")
      case "3" =>
        val inputUser = toNumber(readLine("Wähle die Nummer einer deiner Karten: "))
        if(inputUser >= 0 && inputUser < 3) {
          val inputTable = toNumber(readLine("Wähle die Nummer einer der Karten auf dem Tisch: "))
          if (inputTable >= 0 && inputTable < 3) {
            val afterTradeGameState = GameLogic.trade(gameState, inputUser, inputTable, currentPlayer)
            displayGameState(afterTradeGameState)
            return afterTradeGameState
          } else {
            println("Falsche Eingabe, versuche es erneut.")
            playerActionHandler(currentPlayer, gameState)
          }
        } else {
          println("Falsche Eingabe, versuche es erneut.")
          playerActionHandler(currentPlayer, gameState)
        }
        println(s"Spieler ${currentPlayer.name} hat getauscht")
      case _ =>
        println("Falsche Eingabe, versuche es erneut.")
        playerActionHandler(currentPlayer, gameState)
    }
    gameState
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
