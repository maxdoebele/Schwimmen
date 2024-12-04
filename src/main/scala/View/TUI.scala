package View

import Controller.*
import Controller.util.Controller
import Model.*
import util._
import scala.io.StdIn.readLine

class TUI(val controller: Controller) extends Observer {

  controller.add(this)

  override def update(): Unit = {
    val currentPlayer = HelpFunctions.getCurrentPlayer(controller.gameState)

    println(s"${currentPlayer.name}, Du bist dran! Wähle eine Aktion: 1 = Klopfen, 2 = Schieben, 3 = Tauschen, undo = letzter Zug rückgängig")
    readLine("Gib eine Nummer ein: ") match {
      case "1" =>
        controller.knock()
      case "2" =>
        println(s"Spieler ${currentPlayer.name} hat geschoben")
        controller.skip()
      case "3" =>
        println("1: Alle Karten tauschen, 2: Eine Karte tauschen")
        readLine("Gib eine Nummer ein: ") match {
          case "1" =>
            controller.tradeAll()
          case "2" =>
            println("0: erste Karte, 1: mittlere Karte, 2: letzte Karte")
            val input = readLine("Gib einmal die Zahl für dein Deck und einmal die Zahl für das Tischdeck ein (0 0), (0 1) etc: ")
            val indices = input.split(" ").map(_.toInt)
            controller.tradeOne(indices(0), indices(1))
          case _ =>
            println("Falsche Eingabe, versuche es erneut.")
            update()
        }
      case "undo" =>
        controller.undo()
      case "redo" =>
        controller.redo()
      case _ =>
        println("Falsche Eingabe, versuche es erneut.")
        update()
    }
  }

  def drawCard(card: Card): List[String] = {
    val suitSymbol = card.suit match {
      case "Herz" => "♥"
      case "Pik" => "♠"
      case "Karo" => "♦"
      case "Kreuz" => "♣"
      case _ => "?"
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
