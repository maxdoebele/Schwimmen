package View

import Controller.util.Controller

import scala.util.{Failure, Success, Try}

object InputHandler {
  @volatile private var input: Option[String] = None

  def readLineThread(controller: Controller): Option[String] = {
    controller.threadReadLine = new Thread(() => {
      val result = Try(scala.io.StdIn.readLine())
      result match {
        case Success(value) =>
          input = Some(value) 
        case Failure(exception) =>
      }
    })

    controller.threadReadLine.start()

    controller.threadReadLine.join()
    input
  }
}
