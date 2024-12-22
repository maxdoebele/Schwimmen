package View

import Controller._
import View.gui.GUI
import View.tui.TUI
import _root_.Controller.GameBuilder._
import _root_.Controller.GameBuilder.GameBuilderImpl.BuildNewGame

import scala.io.StdIn._

object Main {
  def main(args: Array[String]): Unit = {

    val currentGameController = Controller(BuildNewGame(Seq.empty).returnGameState())
    val tui = new TUI(currentGameController)
    val tuiFuture = tui.start()
    
    GUI(currentGameController).main(Array.empty)
  }
}
