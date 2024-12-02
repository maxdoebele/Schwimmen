package View

import Controller.Command._
import Model._

import scala.io.StdIn.readLine

class TUI {
  private var undoStack: List[Command] = Nil
  private var redoStack: List[Command] = Nil

  def tuiActionHandler(currentPlayer: User, gameState: GameState): GameState = {
    println(s"${currentPlayer.name}, Du bist dran! Wähle eine Aktion: 1 = Klopfen, 2 = Schieben, 3 = Tauschen, undo = letzter Zug rückgängig")
    readLine("Gib eine Nummer ein: ") match {
      case "1" =>
        println(s"Spieler ${currentPlayer.name} hat geklopft")
        val knockCommand = new KnockCommand(gameState, currentPlayer)
        val afterKnockGameState = knockCommand.execute()
        storeCommand(knockCommand)
        afterKnockGameState

      case "2" =>
        println(s"Spieler ${currentPlayer.name} hat geschoben")
        val skipCommand = new SkipCommand(gameState)
        val afterSkipGameState = skipCommand.execute()
        storeCommand(skipCommand)
        afterSkipGameState
      case "3" =>
        println("1: Alle Karten tauschen, 2: Eine Karte tauschen")
        readLine("Gib eine Nummer ein: ") match {
          case "1" =>
            val tradeAllCommand = new TradeAllCommand(gameState, currentPlayer)
            val afterTradeGameState = tradeAllCommand.execute()
            storeCommand(tradeAllCommand)
            displayGameState(afterTradeGameState)
            afterTradeGameState
          case "2" =>
            println("0: erste Karte, 1: mittlere Karte, 2: letzte Karte")
            val input = readLine("Gib einmal die Zahl für dein Deck und einmal die Zahl für das Tischdeck ein (0 0), (0 1) etc: ")
            val indices = input.split(" ").map(_.toInt)
            val tradeOneCommand = new TradeOneCommand(gameState, currentPlayer, indices(0), indices(1))
            val afterTradeOneGameState = tradeOneCommand.execute()
            storeCommand(tradeOneCommand)
            displayGameState(afterTradeOneGameState)
            afterTradeOneGameState
          case _ =>
            println("Falsche Eingabe, versuche es erneut.")
            tuiActionHandler(currentPlayer, gameState)
        }
      case "undo" =>
        undo(gameState).getOrElse {
          println("Es gibt keinen Zug zum Rückgängig machen!")
          gameState
        }
      case "redo" =>
        redo(gameState).getOrElse {
          println("Es gibt keinen Zug zum Rückgängig machen!")
          gameState
        }
      case _ =>
        println("Falsche Eingabe, versuche es erneut.")
        tuiActionHandler(currentPlayer, gameState)
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

  def storeCommand(command: Command): Unit = {
    undoStack = command :: undoStack
    redoStack = List.empty
  }

  def undo(gameState: GameState): Option[GameState] = {
    undoStack match {
      case Nil =>
        println("Kein Zug zum Rückgängig machen.")
        None 
      case lastCommand :: remainingUndoStack =>
        lastCommand.undoStep() // Undo the last command
        redoStack = lastCommand :: redoStack // Move the undone command to the redo stack
        undoStack = remainingUndoStack // Remove the command from the undo stack
        println("Zug wurde erfolgreich rückgängig gemacht!")
        Some(lastCommand)
    }
  }

  def redo(currentGameState: GameState): Option[GameState] = {
    redoStack match {
      case Nil =>
        println("Kein Zug zum Wiederherstellen.")
        None
      case lastCommand :: remainingRedoStack =>
        val nextState = lastCommand.redoStep() // Execute the redo of the command
        undoStack = lastCommand :: undoStack // Add the command back to the undo stack
        redoStack = remainingRedoStack // Remove it from the redo stack
        println("Zug wurde erfolgreich wiederhergestellt!")
        Some(nextState) // Return the new game state after redo
    }
  }


}
