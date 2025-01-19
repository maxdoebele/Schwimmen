package ControllerTest.FileIOTest

import Controller.GameBuilder.GameBuilderImpl.BuildNewGame
import FileIO.FileIOImpl.FileIOJSON
import Model.BaseImpl.GameState
import Model.GameStateTrait
import org.scalatest._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.io.File

class FileIOJSONTest extends AnyWordSpec with Matchers {

  "FileIOJSON" should {

    "create and read back the correct GameState" in {

      val playerNames = Seq("Player1", "Player2", "Player3", "Player4")
      val gameState = BuildNewGame(playerNames).returnGameState()

      val fileIO = new FileIOJSON

      fileIO.createFile(gameState)

      val readGameState: GameStateTrait = fileIO.readFile()

      assert(readGameState.asInstanceOf[GameState].players.size == 4, "The gamestate should have 4 players.")
      assert(readGameState.asInstanceOf[GameState].table.name.equals("theTable"), "The table should have the name theTable.")
      assert(readGameState.asInstanceOf[GameState].table.lifePoints.equals(-1), "The table should have -1 life points.")
      assert(readGameState.asInstanceOf[GameState].deck.cardDeck.size == (32 - 5 * 3), "The carddeck should have 32 cards.")
      assert(readGameState.asInstanceOf[GameState].players.map(_.name).equals(playerNames), "The players should have the names Player1, Player2, Player3, Player4.")
      assert(readGameState.asInstanceOf[GameState].players.map(_.lifePoints).equals(Seq(3, 3, 3, 3)), "The players should have 3 life points.")
      assert(readGameState.asInstanceOf[GameState].gameOver.equals(false), "The game should not be over.")

      // Optionally, clean up the file after test
      new File("src/main/data/gameState.json").delete()
    }
  }
}

