package FileIO.FileIOImpl

import FileIO.FileIO
import Model.BaseImpl.GameState
import Model.GameStateTrait
import play.api.libs.json
import play.api.libs.json.{JsValue, Json}

import java.io.{File, PrintWriter}
import scala.io.Source

class FileIOJSON extends FileIO {

  override def createFile(gameState: GameState): Unit = {
    val json: JsValue = Json.toJson(gameState)

    val pw = new PrintWriter(new File("src/main/data/gameState.json"))
    pw.write(s"$json")
    pw.close
  }

  def readFile(fileName: String): GameStateTrait = {
    val source = Source.fromFile(fileName)
    val fileContent = try source.mkString finally source.close()
    
    val json: JsValue = Json.parse(fileContent)
    
    val parsedGameState: GameState = json.as[GameState] // Requires an implicit Reads[GameState]

    // Return the parsed GameState (as GameStateTrait)
    parsedGameState
  }

  
}
