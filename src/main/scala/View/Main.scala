package View

import Model.User

import scala.io.StdIn._
import Model._
import Controller._

object Main {
  def main(args: Array[String]): Unit = {
    println("Willkommen bei Schwimmen :)")

    val namen = readLine("Bitte sag mir die Namen der Mitspieler: ").split(" ").toList
    val currentGame = GameManage.initializeNewGame(namen)
    val tui = new TUI()
    tui.displayGameState(currentGame)

    GameManage.playGame(currentGame)
  }
}
