package View.tui

import scala.concurrent.Future

trait InputHandlerTrait {
  def startReading(): Unit
  def readLineThread(): Future[String]
}
