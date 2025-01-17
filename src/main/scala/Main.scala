import Controller._
import View.gui.GUI
import View.tui.TUI
import _root_.Controller.DependencyInjection.GameModule
import com.google.inject.Guice

object Main {
  def main(args: Array[String]): Unit = {
    val injector = Guice.createInjector(new GameModule)

    injector.getInstance(classOf[Controller])
    injector.getInstance(classOf[TUI]).start()
    injector.getInstance(classOf[GUI]).main(Array.empty)
  }
}
