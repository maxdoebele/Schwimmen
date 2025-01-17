package View.tui

import scala.concurrent.{Future, Promise}
import scala.io.StdIn

class InputHandlerImpl extends InputHandlerTrait {
  private var inputPromise: Option[Promise[String]] = None

  override def startReading(): Unit = {
    new Thread(() => {
      while (true) {
        val line = StdIn.readLine()
        this.synchronized {
          inputPromise.foreach(_.success(line))
          inputPromise = None // Reset the promise
        }
      }
    }).start()
  }

  override def readLineThread(): Future[String] = this.synchronized {
    if (inputPromise.isDefined) {
      inputPromise.get.failure(new IllegalStateException())
    }
    val promise = Promise[String]()
    inputPromise = Some(promise)
    promise.future
  }

}
