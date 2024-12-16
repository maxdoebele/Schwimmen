package View

import Controller.util.Controller
import Controller.util.Observer
import _root_.Controller.HelpFunctions
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.layout.StackPane
import scalafx.scene.paint.Color.*

class GUI(controller: Controller) extends JFXApp3, Observer {

  controller.add(this)

  override def update(): Unit = {
    println("do nothing")
  }
  override def start(): Unit = {

    stage = new JFXApp3.PrimaryStage {
      title.value = "Hello Stage"
      width = 600
      height = 450
      scene = new Scene {
        fill = LightGreen
        content = new StackPane {
          children = new Button {
            text = "Click Me"
            onAction = handle {
              println("Button clicked!")
            }
          }
        }
      }
    }
  }
}