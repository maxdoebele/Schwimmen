import Controller.DependencyInjection.GameModule
import Controller.Controller
import View.gui.GUI
import View.tui.TUI
import com.google.inject.{Guice, Injector}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MainTest extends AnyFlatSpec with Matchers {

  "Main" should "initialize the necessary components" in {
    // Create the Guice injector with the GameModule
    val injector: Injector = Guice.createInjector(new GameModule)

    // Get instances of the components
    val controller = injector.getInstance(classOf[Controller])
    val tui = injector.getInstance(classOf[TUI])
    val gui = injector.getInstance(classOf[GUI])

    // Verify that the components are not null (i.e., they are correctly instantiated)
    controller should not be null
    tui should not be null
    gui should not be null
  }
}
