package FileIO.FileIOImpl

import FileIO.FileIO
import Model.BaseImpl.GameState
import Model.GameStateTrait
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import java.io.{File, IOException}

class FileIOYAML extends FileIO {

  private final val FILEPATH = "src/main/data/gameState.yaml"

  private val mapper: ObjectMapper = new ObjectMapper(new YAMLFactory())
  mapper.registerModule(DefaultScalaModule)

  override def createFile(gameState: GameState): Unit = {
    try {
      val yamlFile = new File("src/main/data/gameState.yaml")
      mapper.writeValue(yamlFile, gameState)
      println(s"Spielzustand erfolgreich in ${yamlFile.getAbsolutePath} gespeichert.")
    } catch {
      case e: IOException =>
        println(s"Fehler beim Speichern der YAML-Datei: ${e.getMessage}")
    }
  }

  override def readFile(): GameStateTrait = {
    try {
      val yamlFile = new File(FILEPATH)
      mapper.readValue(yamlFile, classOf[Model.BaseImpl.GameState])
    } catch {
      case e: IOException =>
        println(s"Fehler beim Lesen der YAML-Datei: ${e.getMessage}")
        throw e
    }
  }
}

