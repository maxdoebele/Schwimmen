package FileIO.FileIOImpl

import FileIO.FileIO
import Model.GameStateTrait
import Model.BaseImpl.{Card,CardDeck,GameState,User}

import java.io.{File, PrintWriter}

class FileIOXML extends FileIO {
  
  override def createFile(gameState :GameStateTrait) : Unit = {
     val xml = <gameState><players> {gameState.players.map(player => player.toXML)} </players> 
       <table>{gameState.table.toXML}</table>
       <deck>{gameState.deck.toXML()}</deck>
       <queue>{gameState.queue}</queue>
       <knockCounter>{gameState.knockCounter}</knockCounter>
       <gameOver>{gameState.gameOver}</gameOver>
       <indexCardPlayer>{gameState.indexCardPlayer}</indexCardPlayer>
       <indexCardTable>{gameState.indexCardTable}</indexCardTable>
       <swimmerGlobal>{gameState.schwimmer}</swimmerGlobal>
       <roundCounter>{gameState.roundCounter}</roundCounter>
       <lastLooser>{gameState.lastLoosers.map(looser=>looser.toXML)}</lastLooser>
     </gameState>

    val pw = new PrintWriter(new File("src/main/data/gameState.xml"))
    pw.write(s"$xml")
    pw.close
  }

  override def readFile(filePath: String): GameStateTrait = {
    import scala.xml.XML

    val xml = XML.loadFile(filePath)

    val players = (xml \ "players" \ "player").map(node => User.fromXML(node)).toList

    val table = User.fromXML((xml \ "table").head)
    val deck = CardDeck.fromXML((xml \ "deck").head)
    val queue = (xml \ "queue").text.trim.toInt
    val knockCounter = (xml \ "knockCounter").text.trim.toInt
    val gameOver = (xml \ "gameOver").text.trim.toBoolean
    val indexCardPlayer = (xml \ "indexCardPlayer").text.trim.toInt
    val indexCardTable = (xml \ "indexCardTable").text.trim.toInt
    val swimmer = (xml \ "swimmerGlobal").text.trim.toBoolean
    val roundCounter = (xml \ "roundCounter").text.trim.toInt
    val lastLoosers = (xml \ "lastLooser" \ "looser").map(node => User.fromXML(node)).toList

    // Reconstruct the GameState
    GameState(
      players = players,
      table = table,
      deck = deck,
      queue = queue,
      knockCounter = knockCounter,
      gameOver = gameOver,
      indexCardPlayer = indexCardPlayer,
      indexCardTable = indexCardTable,
      schwimmer = swimmer,
      roundCounter = roundCounter,
      lastLoosers = lastLoosers
    )
  }

}
