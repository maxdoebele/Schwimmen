package ControllerTest.FileIOTest
import Controller.GameBuilder.GameBuilderImpl.BuildNewGame
import FileIO.FileIOImpl.FileIOXML
import FileIO.FileIO
import Model.BaseImpl.{Card, CardDeck, GameState, User}
import Model.GameStateTrait
import org.scalatest.wordspec.AnyWordSpec

class FileIOXMLTest extends AnyWordSpec {

  val player1 = User(Seq(Card("Herz", "7"), Card("Pik", "10"), Card("Karo", "K")), 2, "Player1")
  val player2 = User(Seq(Card("Kreuz", "7"), Card("Herz", "10"), Card("Herz", "K")), 3, "Player2")
  val table = User(Seq(Card("Karo", "8"), Card("Herz", "9"), Card("Kreuz", "A")), -1, "Table")
  val gameState = GameState(
    players = Seq(player1, player2),
    table = table,
    deck = new CardDeck().shuffleDeck(),
    queue = 1,
    knockCounter = 0,
    gameOver = false,
    indexCardPlayer = 0,
    indexCardTable = 0,
    schwimmer = false,
    roundCounter = 0,
    lastLoosers = Seq.empty
  )

  "FileIOXML" should {

    "create a file and read from it" in {
      val fileIO = new FileIOXML()
      val players = Seq("Player1", "Player2")
      val gameState = BuildNewGame(players).returnGameState()

      fileIO.createFile(gameState)
      val readGameState: GameStateTrait = fileIO.readFile()
      assert(gameState == readGameState, "XML gameState should be the same")
      assert(gameState.players == readGameState.players, "XML players should be the same")
      assert(gameState.table == readGameState.table, "XML table should be the same")
      assert(gameState.deck == readGameState.deck, "XML deck should be the same")
      assert(gameState.queue == readGameState.queue, "XML queue should be the same")
      assert(gameState.knockCounter == readGameState.knockCounter, "XML knockCounter should be the same")
      assert(gameState.gameOver == readGameState.gameOver, "XML gameOver should be the same")
      assert(gameState.indexCardPlayer == readGameState.indexCardPlayer, "XML indexCardPlayer should be the same")
      assert(gameState.indexCardTable == readGameState.indexCardTable, "XML indexCardTable should be the same")
      assert(gameState.schwimmer == readGameState.schwimmer, "XML schwimmer should be the same")
      assert(gameState.roundCounter == readGameState.roundCounter, "XML roundCounter should be the same")
      assert(gameState.lastLoosers == readGameState.lastLoosers, "XML lastloosers should be the same")
    }
  }
}
