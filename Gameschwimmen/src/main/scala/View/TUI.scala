package View

import Controller.GameLogic
import Model._

import scala.io.StdIn.readLine

class TUI {

  def playerActionHandler(currentPlayer: User, gameState: GameState): GameState = {
    println(s"${currentPlayer.name}, it's your turn! Choose an action: 1 = Knock, 2 = Skip, 3 = Trade")
    readLine("Enter your number: ") match {
      case "1" =>
        val afterKnockGameState = GameLogic.knock(gameState)
        println(s"Spieler ${currentPlayer.name} has knocked")
        return afterKnockGameState
      case "2" =>
        //skip()
        println(s"Spieler ${currentPlayer.name} has skipped")
      case "3" =>
        val inputUser = toNumber(readLine("Select the number of one of your cards to trade: "))
        if(inputUser >= 0 && inputUser < 3) {
          val inputTable = toNumber(readLine("Select the number of one of the table cards to trade: "))
          if (inputTable >= 0 && inputTable < 3) {
            val afterTradeGameState = GameLogic.trade(gameState, inputUser, inputTable, currentPlayer)
            displayGameState(afterTradeGameState)
            return afterTradeGameState
          } else {
            println("Invalid input. Please try again.")
            playerActionHandler(currentPlayer, gameState)
          }
        } else {
          println("Invalid input. Please try again.")
          playerActionHandler(currentPlayer, gameState)
        }
        println(s"Spieler ${currentPlayer.name} has traded")
      case _ =>
        println("Invalid action. Please try again.")
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
