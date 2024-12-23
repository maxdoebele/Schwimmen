package View.tui

import _root_.Controller._
import Model._
import Model.BaseImpl._
import _root_.Controller.HelpFunctions._
import _root_.util.Observer
import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

class TUI(val controller: Controller) extends Observer {

  controller.add(this)

  private val roundCounter: AtomicInteger = new AtomicInteger(1)

  val wrongInputMessage = "Ungültige Eingabe, bitte versuche es erneut."

  def start(): Future[Unit] = {
    Future {
      this.startGame()
      // aufruf von update() nicht mehr nötig da es eh von observer ausgeführt wird
    }
  }

  override def update(): Unit = {
    Future {
      if(controller.gameState.roundCounter == roundCounter.get()) {
        displayEndOfRound()
        this.synchronized {
          roundCounter.incrementAndGet()
        }
      }
      if (controller.gameState.players.size > 1) {
        val currentPlayer = getCurrentPlayer(controller.gameState)
        displayGameState(controller.gameState)

        println(s"${currentPlayer.name}, Du bist dran! Wähle eine Aktion: 1 = Klopfen, 2 = Schieben, 3 = Tauschen, undo = letzter Zug rückgängig")

        InputHandler.readLineThread(controller) match {
          case Some("1") =>
            println(f"${currentPlayer.name} klopft!")
            controller.knock()
          case Some("2") =>
            println(s"Spieler ${currentPlayer.name} hat geschoben")
            controller.skip()
          case Some("3") =>
            println("1: Alle Karten tauschen, 2: Eine Karte tauschen")
            InputHandler.readLineThread(controller) match {
              case Some("1") =>
                controller.tradeAll()
              case Some("2") =>
                println("Schreibe deine und dann die Karte des Tisches mit welcher du Tauschen willst, Beispiel: 0 1")
                println("0: erste Karte, 1: mittlere Karte, 2: letzte Karte.")
                val input = InputHandler.readLineThread(controller)
                input match {
                  case Some(indices) =>
                    val splitInput = indices.split(" ").map(_.toInt)
                    if (splitInput.length == 2) {
                      controller.tradeOne(splitInput(0), splitInput(1))
                    } else {
                      println(wrongInputMessage)
                      update()
                    }
                  case None =>
                    //println("Eingabe wurde abgebrochen oder fehlgeschlagen.")
                    update()
                }
              case _ =>
                println(wrongInputMessage)
                update()
            }
          case Some("undo") =>
            controller.undo()
          case Some("redo") =>
            controller.redo()
          case None =>
          //println("Eingabe wurde abgebrochen.")
          case _ =>
            println(wrongInputMessage)
            update()
        }
      }
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

  def playerInput(): Seq[String] = {
    println("Bitte gib die Namen der Spieler getrennt durch Leerzeichen ein")
    val namesInput = scala.io.StdIn.readLine()
    namesInput.split(" ").toList.filter(_.nonEmpty)
  }

  def startGame(): Unit = {
    val playerNames = playerInput()
    if (controller.getPlayerNames.nonEmpty) {
      println("Spiel wird mit den folgenden Spielern gestartet:")
      println(controller.getPlayerNames.mkString(", "))
    } else if (playerNames.nonEmpty) {
      controller.setPlayerNames(playerNames)
    } else {
      println(wrongInputMessage)
      startGame()
    }
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
