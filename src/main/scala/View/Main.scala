package View

import Controller.*
import View.gui.GUI
import View.tui.TUI
import _root_.Controller.GameBuilder.*
import _root_.Controller.GameBuilder.GameBuilderImpl.BuildNewGame

import scala.io.StdIn.*

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
