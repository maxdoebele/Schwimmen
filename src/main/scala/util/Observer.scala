package util

trait Observer {
  def update(): Unit
  def saveGame(): Unit
  def loadGame(): Unit
}