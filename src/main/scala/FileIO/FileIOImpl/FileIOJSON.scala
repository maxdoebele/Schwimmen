package FileIO.FileIOImpl

import FileIO.FileIO
import Model.BaseImpl.GameState
import Model.GameStateTrait
import play.api.libs.json
import play.api.libs.json.{JsValue, Json}

import java.io.{File, PrintWriter}
import scala.io.Source

class FileIOJSON extends FileIO {

  private final val FILEPATH = "src/main/data/gameState.json"

  override def createFile(gameState: GameState): Unit = {
    val json: JsValue = Json.toJson(gameState)

    val prettyJson: String = Json.prettyPrint(json)

    val pw = new PrintWriter(new File(FILEPATH))
    pw.write(s"$prettyJson")
    pw.close
  }

  def readFile(): GameStateTrait = {
    val source = Source.fromFile(FILEPATH)
    val fileContent = try source.mkString finally source.close()

    val json: JsValue = Json.parse(fileContent)

    val parsedGameState: GameState = json.as[GameState]

    // Return the parsed GameState (as GameStateTrait)
    parsedGameState
  }
}
