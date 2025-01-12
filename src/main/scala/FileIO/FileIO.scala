package FileIO

import Model.GameStateTrait

trait FileIO {

  def createFile(gameStateTrait: GameStateTrait): Unit

  def readFile(fileName:String): GameStateTrait
}


