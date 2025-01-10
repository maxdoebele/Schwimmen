package View

import Controller._
import _root_.Controller.DependencyInjection.GameModule
import View.gui.GUI
import View.tui.TUI
import _root_.Controller.GameBuilder._
import _root_.Controller.GameBuilder.GameBuilderImpl.BuildNewGame
import com.google.inject.Guice
import com.google.inject.name.Names

import scala.io.StdIn._

object Main {
  def main(args: Array[String]): Unit = {
    val injector = Guice.createInjector(new GameModule)

    injector.getInstance(classOf[Controller])
    injector.getInstance(classOf[TUI]).start()
    injector.getInstance(classOf[GUI]).main(Array.empty)
  }
}
