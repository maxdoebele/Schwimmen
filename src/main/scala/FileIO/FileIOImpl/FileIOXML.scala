package FileIO.FileIOImpl

import FileIO.FileIO
import Model.GameStateTrait
import Model.BaseImpl.{Card,CardDeck,GameState,User}
import scala.xml.XML

import java.io.{File, PrintWriter}

class FileIOXML extends FileIO {
  
  override def createFile(gameState :GameState) : Unit = {
     val xml = gameState.toXML

      val pw = new PrintWriter(new File("src/main/data/gameState.xml"))
      pw.write(s"$xml")
      pw.close()
  }

  override def readFile(filePath: String): GameStateTrait = {
    val xml = XML.loadFile(filePath)
    val gameState = GameState.fromXML(xml)
    gameState
  }

}
