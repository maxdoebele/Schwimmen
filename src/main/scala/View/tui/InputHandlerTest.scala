package View.tui

import scala.concurrent.Future

class InputHandlerTest extends InputHandlerTrait {

  var inputBuffer: String = ""
  var turnBufferOff: Boolean = true
  override def startReading(): Unit = {
    // do nothing
  }

  override def readLineThread(): Future[String] = {
    if (turnBufferOff) {
      turnBufferOff = false
      Future.successful(inputBuffer)
    }
    else
    Future.failed(new Exception("Buffer is empty"))
  }

  def fillInputBuffer(input: String, boolean: Boolean): Unit = {
    inputBuffer = input
    turnBufferOff = boolean
  }
}
