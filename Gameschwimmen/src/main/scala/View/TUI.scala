package View

import Controller.GameLogic
import Model._

import scala.io.StdIn.readLine

class TUI {

  def playerActionHandler(player: User, gameState: GameState): GameState = {
    println(s"${player.name}, it's your turn! Choose an action: 1 = Knock, 2 = Skip, 3 = Trade")
    val action = readLine("Enter your number: ") match {
      case "1" =>
        val afterKnockGameState = GameLogic.knock(gameState)
        println(s"Spieler ${player.name} hat geklopft")
        afterKnockGameState
      case "2" =>
        //skip()
        println(s"Spieler ${player.name} hat geschoben")
      case "3" =>
        //trade()
        println(s"Spieler ${player.name} hat getauscht")
      case _ =>
        println("Invalid action. Please try again.")
        playerActionHandler(player, gameState) // Retry on invalid input
    }
    gameState
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
      println(s"${player.name}'s hand:")
      displayHand(player.handDeck)
      println()
    }

    println("Table's hand:")
    displayHand(gameState.table.handDeck)
    println() // Line break after table hand
  }

  private def displayHand(handDeck: Seq[Card]): Unit = {
    val drawnCards = handDeck.map(drawCard)
    val transposedLines = drawnCards.transpose
    transposedLines.foreach(row => println(row.mkString(" ")))
  }
}
