package View

import scala.io.StdIn._
import Controller._
import util._
import Controller.GameBuilder._

object Main {
  def main(args: Array[String]): Unit = {
    println("Willkommen bei Schwimmen :)")

    val namen = readLine("Bitte sag mir die Namen der Mitspieler: ").split(" ").toList
    val currentGameConroller = Controller(BuildNewGame(namen).returnGameState())
    playGame(currentGameConroller)
    print("Das Spiel ist vorbei")
    
  }

  def playGame(controller: Controller): Controller = {
    //GUI(currentGameConroller).main(array.empty)
    val tui = TUI(controller)
    
    while (controller.gameState.players.size > 1) {
      tui.update()
      resetGame(controller)
    }
    controller
  }

  def resetGame(controller: Controller): Unit = {
    controller.gameState = BuildNewRound(controller.gameState).returnGameState()
  }
}
