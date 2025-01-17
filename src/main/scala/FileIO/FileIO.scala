package FileIO

import Model.BaseImpl.GameState
import Model.GameStateTrait

/**
 * Trait for FileIO
 * Implements methods to create and read a file
 * @see FileIOImpl
 */

trait FileIO {

  def createFile(gameStateTrait: GameState): Unit

  def readFile(): GameStateTrait
}


