package FileIO.FileIOImpl

import FileIO.FileIO
import Model.GameStateTrait
import Model.BaseImpl.{Card,CardDeck,GameState,User}
import scala.xml.XML

import java.io.{File, PrintWriter}

class FileIOXML extends FileIO {
  private final val FILEPATH = "src/main/data/gameState.xml"
  
  override def createFile(gameState :GameState) : Unit = {
     val xml = gameState.toXML

      val pw = new PrintWriter(new File(FILEPATH))
      pw.write(s"$xml")
      pw.close()
  }

  override def readFile(): GameStateTrait = {
    val xml = XML.loadFile(FILEPATH)
    val gameState = GameState.fromXML(xml)
    gameState
  }

}
