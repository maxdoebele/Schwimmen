package View.tui

import _root_.Controller._
import Model._
import Model.BaseImpl._
import _root_.Controller.HelpFunctions._
import _root_.util.Observer
import com.google.inject.Inject

import scala.util.{Failure, Success}
import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.*
import scala.concurrent.ExecutionContext.Implicits.global

class TUI @Inject() (val controller: Controller, val inputHandler: InputHandlerTrait) extends Observer {

  controller.add(this)
  inputHandler.startReading()

  private val roundCounter: AtomicInteger = new AtomicInteger(1)
  private val wrongInputMessage = "Ungültige Eingabe, bitte versuche es erneut."

  def start(): Future[Unit] = {
    Future {
      println("Bitte gib die Namen der Spieler mit Komma getrennt ein.")
      inputHandler.readLineThread().onComplete {
        case Success(input) =>
          val names = input.split(", ").toList
          if(checkForPlayerLimit(names)) {
            controller.createNewGame(names)
          } else {
            println("Ungültige Eingabe. Es müssen zwischen 2 und 9 Spieler sein.")
            start()
          }
        case Failure(exception) =>
        // cancel input from GUI
      }
    }
  }

  override def update(): Unit = {
    Future {
      if (controller.gameState.roundCounter == roundCounter.get()) {
        displayEndOfRound()
        println("Drücke Enter um weiter zu spielen")
        inputHandler.readLineThread().onComplete {
          case Success(input) =>
            input match
              case _ =>
                println("Weiter gehts...")
                controller.notifySubscribers()
          case Failure(exception) =>
            //
          }
        this.synchronized {
          roundCounter.incrementAndGet()
        }
      }
      else if (controller.gameState.players.size > 1) {
        val currentPlayer = getCurrentPlayer(controller.gameState)
        displayGameState(controller.gameState)

        println(s"${currentPlayer.name}, Du bist dran! Wähle eine Aktion: \n" +
          s"1 = Klopfen, 2 = Schieben, 3 = Tauschen, undo = letzter Zug rückgängig")

        inputHandler.readLineThread().onComplete {
          case Success(input) =>
            input match {
              case "1" =>
                if(controller.gameState.queue < controller.gameState.players.size) {
                  println("In der ersten Runde kann nicht geklopft werden.")
                  update()
                } else {
                  println(f"${currentPlayer.name} klopft!")
                  controller.knock()
                }
              case "2" =>
                println(s"Spieler ${currentPlayer.name} hat geschoben")
                controller.skip()
              case "3" =>
                handleTrade()
              case "undo" =>
                controller.undo()
              case "redo" =>
                controller.redo()
              case "save" =>
                controller.saveGame()
              case "load" =>
                controller.loadGame()
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

    inputHandler.readLineThread().onComplete {
      case Success(input) =>
        input match {
          case "1" =>
            controller.tradeAll()
          case "2" =>
            println("Schreibe deine und dann die Karte des Tisches mit welcher du Tauschen willst, Beispiel: 0 1")
            println("0: erste Karte, 1: mittlere Karte, 2: letzte Karte.")

            inputHandler.readLineThread().onComplete {
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

  private def drawCard(card: Card): List[String] = {
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

  private def displayGameState(gameState: GameStateTrait): Unit = {
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

  private def displayEndOfRound(): Unit = {
    val losers = controller.gameState.lastLoosers.map(_.name).mkString(", ")
    println(s"Verloren hat: $losers")

    if (controller.gameState.players.size > 1) {
      val currentScores = HelpFunctions.calculateCurrentScore(controller)
      println("Aktueller Punktestand")
      val currentScoresZip = currentScores.zipWithIndex
      currentScoresZip.foreach { case ((name, score), index) =>
        val points = controller.gameState.playerPoints(index)
        println(f"$name%-20s $score%10d $points%10.2f")
      }
      for (i <- 1 to 3) {
        println()
      }
    } else {
      println(f"Das Spiel ist vorbei... Gratuliere ${controller.gameState.players.map(_.name).mkString(", ")} du hast GEWONNEN!")
    }
  }
}
