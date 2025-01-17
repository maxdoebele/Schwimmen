import Controller.GameBuilder.GameBuilderImpl.BuildNewGame
import FileIO.FileIOImpl.FileIOYAML
import Model.BaseImpl.GameState
import Model.GameStateTrait
import org.scalatest._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import java.io.{File, IOException}

class FileIOYAMLTest extends AnyWordSpec with Matchers {

  "FileIOYAML" should {

    "create and read back the correct GameState" in {
      val player = Seq("Player1", "Player2", "Player3", "Player4")
      val gameState = BuildNewGame(player).returnGameState()

      val fileIO = new FileIOYAML

      fileIO.createFile(gameState)

      val readGameState: GameStateTrait = fileIO.readFile()

      readGameState shouldBe a[GameState]
      assert(readGameState.asInstanceOf[GameState].players.size.equals(4), "The gamestate should have 4 players.")
      assert(readGameState.asInstanceOf[GameState].table.name.equals("theTable"), "The table should have the name theTable.")
      assert(readGameState.asInstanceOf[GameState].table.lifePoints.equals(-1), "The table should have -1 life points.")
      assert(readGameState.asInstanceOf[GameState].deck.cardDeck.size.equals(32 - 5 * 3), "The carddeck should have 32 cards.")
      assert(readGameState.asInstanceOf[GameState].players.map(_.name).equals(player), "The players should have the names Player1, Player2, Player3, Player4.")
      assert(readGameState.asInstanceOf[GameState].players.map(_.lifePoints).equals(Seq(3, 3, 3, 3)), "The players should have 3 life points.")
      assert(readGameState.asInstanceOf[GameState].gameOver.equals(false), "The game should not be over.")

      // Optionally, clean up the file after test
      new File("src/main/data/gameState.yaml").delete()
    }

  }
}
