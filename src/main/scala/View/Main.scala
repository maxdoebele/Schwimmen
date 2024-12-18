package View

import scala.io.StdIn._
import Controller._
import util._
import _root_.Controller.GameBuilder._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Main {
  def main(args: Array[String]): Unit = {
    println("Willkommen bei Schwimmen :)")

    val namen = readLine("Bitte sag mir die Namen der Mitspieler: ").split(" ").toList
    val currentGameConroller = Controller(BuildNewGame(namen).returnGameState())

    val tui = new TUI(currentGameConroller)
    val tuiFuture = tui.start()
    
    GUI(currentGameConroller).main(Array.empty)
  }

}
