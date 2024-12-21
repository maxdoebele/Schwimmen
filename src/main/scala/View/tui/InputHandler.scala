package View.tui

import Controller.Controller

import java.util.concurrent.atomic.AtomicReference
import scala.util.{Failure, Success, Try}

object InputHandler {
  var input = new AtomicReference[Option[String]](None)

  def resetInput(): Unit = {
    input.set(None)
  }


  def readLineThread(controller: Controller): Option[String] = {
    controller.threadReadLine = new Thread(() => {
      val result = Try(scala.io.StdIn.readLine())
      result match {
        case Success(value) =>
          input.set(Some(value))
        case Failure(exception) =>
          resetInput()
      }
    })

    controller.threadReadLine.start()
    controller.threadReadLine.join()
    input.get()
  }
}
