package FileIO

import Model.BaseImpl.GameState
import Model.GameStateTrait

trait FileIO {

  def createFile(gameStateTrait: GameState): Unit

  def readFile(fileName:String): GameStateTrait
}


