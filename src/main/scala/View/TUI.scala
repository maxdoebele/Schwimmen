package View

import Controller.{GameLogic, playerActions}
import Model.*

import scala.io.StdIn.readLine

class TUI {
  

  object knockState extends playerActions {
    override def playerAction(currentPlayer: User, gameState: GameState): GameState = {
      val afterKnockGameState = GameLogic.knock(gameState)
      afterKnockGameState
    }
  }

  object skipState extends playerActions {
    override def playerAction(currentPlayer: User, gameState: GameState): GameState = {
      gameState
    }
  }

  object tradeState extends playerActions {
    override def playerAction(currentPlayer: User, gameState: GameState): GameState = {
      println("1 = eine Karte tauschen, 2 = alle drei Karten tauschen")
      readLine("Gib eine Nummer ein: ") match {
        case "1" =>
          val inputUser = toNumber(readLine("Wähle die Nummer einer deiner Karten: "))
          if (inputUser >= 0 && inputUser < 3) {
            val inputTable = toNumber(readLine("Wähle die Nummer einer der Karten auf dem Tisch: "))
            if (inputTable >= 0 && inputTable < 3) {
              val afterTradeGameState = GameLogic.tradeOneCard(gameState, inputUser, inputTable, currentPlayer)
              displayGameState(afterTradeGameState)
              println(s"Spieler ${currentPlayer.name} hat eine Karte getauscht")
              afterTradeGameState
            } else {
              println("Falsche Eingabe, versuche es erneut.")
              playerAction(currentPlayer, gameState)
            }
          } else {
            println("Falsche Eingabe, versuche es erneut.")
            playerAction(currentPlayer, gameState)
          }
        case "2" =>
          val afterTradeAllGameState = GameLogic.tradeAllCards(gameState, currentPlayer)
          displayGameState(afterTradeAllGameState)
          println(s"Spieler ${currentPlayer.name} hat alle Karten getauscht")
          afterTradeAllGameState
        case _ =>
          println("Falsche Eingabe, versuche es erneut.")
          playerAction(currentPlayer, gameState)
      }
    }
  }

  class PlayerActionHandler {
    private var state: playerActions = _
    def setState(newState: playerActions): Unit = {
      state = newState
    }
    def playerAction(currentPlayer: User, gameState: GameState): GameState = {
      state.playerAction(currentPlayer, gameState)
    }
  }

  def playerActionHandler(currentPlayer: User, gameState: GameState): GameState = {
    val actionHandler = new PlayerActionHandler()

    println(s"${currentPlayer.name}, Du bist dran! Wähle eine Aktion: 1 = Klopfen, 2 = Schieben, 3 = Tauschen")
    readLine("Gib eine Nummer ein: ") match {
      case "1" =>
        actionHandler.setState(knockState)
        println(s"Spieler ${currentPlayer.name} hat geklopft")
      case "2" =>
        actionHandler.setState(skipState)
        println(s"Spieler ${currentPlayer.name} hat geschoben")
      case "3" =>
        actionHandler.setState(tradeState)
      case _ =>
        println("Falsche Eingabe, versuche es erneut.")
        return playerActionHandler(currentPlayer, gameState)
    }
    actionHandler.playerAction(currentPlayer, gameState)
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
