package View.tui

import _root_.Controller._
import Model._
import Model.BaseImpl._
import _root_.Controller.HelpFunctions._
import _root_.util.Observer
import com.google.inject.Inject

import scala.util.{Failure, Success}
import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

class TUI @Inject() (val controller: Controller) extends Observer {

  controller.add(this)
  InputHandler.startReading()

  private val roundCounter: AtomicInteger = new AtomicInteger(1)

  val wrongInputMessage = "Ungültige Eingabe, bitte versuche es erneut."

  def start(): Future[Unit] = {
    Future {
      println("Bitte gib die Namen der Spieler mit Leerzeichen getrennt ein.")
      InputHandler.readLineThread().onComplete {
        case Success(input) =>
          val names = input.split(" ").toList
          controller.createNewGame(names)
        case Failure(exception) =>
        // cancel input from GUI
      }
    }
  }

  override def update(): Unit = {
    Future {
      if (controller.gameState.roundCounter == roundCounter.get()) {
        displayEndOfRound()
        this.synchronized {
          roundCounter.incrementAndGet()
        }
      }
      if (controller.gameState.players.size > 1) {
        val currentPlayer = getCurrentPlayer(controller.gameState)
        displayGameState(controller.gameState)

        println(s"${currentPlayer.name}, Du bist dran! Wähle eine Aktion: 1 = Klopfen, 2 = Schieben, 3 = Tauschen, undo = letzter Zug rückgängig")

        InputHandler.readLineThread().onComplete {
          case Success(input) =>
            input match {
              case "1" =>
                println(f"${currentPlayer.name} klopft!")
                controller.knock()
              case "2" =>
                println(s"Spieler ${currentPlayer.name} hat geschoben")
                controller.skip()
              case "3" =>
                handleTrade()
              case "undo" =>
                controller.undo()
              case "redo" =>
                controller.redo()
              case _ =>
                println(wrongInputMessage)
                update()
            }
          case Failure(exception) =>
            //println(s"Failed to read input: ${exception.getMessage}")
        }
      }
    }
  }

  private def handleTrade(): Unit = {
    println("1: Alle Karten tauschen, 2: Eine Karte tauschen")

    InputHandler.readLineThread().onComplete {
      case Success(input) =>
        input match {
          case "1" =>
            controller.tradeAll()
          case "2" =>
            println("Schreibe deine und dann die Karte des Tisches mit welcher du Tauschen willst, Beispiel: 0 1")
            println("0: erste Karte, 1: mittlere Karte, 2: letzte Karte.")

            InputHandler.readLineThread().onComplete {
              case scala.util.Success(indices) =>
                val splitInput = indices.split(" ").map(_.toInt)
                if (splitInput.length == 2) {
                  controller.tradeOne(splitInput(0), splitInput(1))
                } else {
                  println(wrongInputMessage)
                  handleTrade()
                }
              case Failure(exception) =>
                println(s"Failed to read input for trading: ${exception.getMessage}")
            }
          case _ =>
            println(wrongInputMessage)
            handleTrade() // Retry the trade process
        }
      case scala.util.Failure(exception) =>
        println(s"Failed to read input for trading choice: ${exception.getMessage}")
    }
  }

  def drawCard(card: Card): List[String] = {
    val suitSymbol = card.suit match {
      case "Herz" => "♥"
      case "Pik" => "♠"
      case "Caro" => "♦"
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

  def displayGameState(gameState: GameStateTrait): Unit = {
    gameState.players.foreach { player =>
      println(s"${player.name}'s Karten:")
      displayHand(player.handDeck)
      println()
    }

    println("Tisch Karten:")
    displayHand(gameState.table.handDeck)
    println()
  }

  private def displayHand(handDeck: Seq[Card]): Unit = {
    val drawnCards = handDeck.map(drawCard)
    val transposedLines = drawnCards.transpose
    transposedLines.foreach(row => println(row.mkString(" ")))
  }

  def displayEndOfRound(): Unit = {
    val losers = controller.gameState.lastLoosers.map(_.name).mkString(", ")
    println(s"Verloren hat: $losers")

    if (controller.gameState.players.size > 1) {
      val currentScores = HelpFunctions.calculateCurrentScore(controller)
      println("Aktueller Punktestand")
      currentScores.foreach { case (name, score) =>
        println(f"$name%-20s  $score%3d") // Formatierung: Name linksbündig, Punktzahl rechtsbündig
      }
      for (i <- 1 to 3) {
        println()
      }
      println("Neue Runde:")
    } else {
      println(f"Das Spiel ist vorbei... Gratuliere ${controller.gameState.players.map(_.name).mkString(", ")} du hast GEWONNEN!")
    }
  }
}
