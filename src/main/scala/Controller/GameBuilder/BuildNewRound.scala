package Controller.GameBuilder

import Controller.util.Controller
import Model._

class BuildNewRound(gameState: GameState) extends GameBuilder {

  private val cardDeck: CardDeck = createCardDeck()
  private val players: Seq[User] = updatePlayers(gameState.players)
  private val table: User = updateTable()
  private val schwimmer: Boolean = gameState.schwimmer
  private val distibuteCardGameState: GameState = distributeCards(players, table, cardDeck)
  private val updatedGameState: GameState = mergeNewGameState()

  private def mergeNewGameState(): GameState = {
    gameState.copy(players = distibuteCardGameState.players, table = distibuteCardGameState.table, deck = distibuteCardGameState.deck, gameOver = false, schwimmer = this.schwimmer
    )
  }

  
  override def createCardDeck(): CardDeck = {
    new CardDeck().shuffleDeck()
  }

  def updatePlayers(players: Seq[User]): Seq[User] = {
    if (players.nonEmpty) {
      val filteredPlayers = players.filter(user => user.lifePoints > 0)
      filteredPlayers.tail :+ filteredPlayers.head
    } else {
      players
    }
  }

  override def updateTable(): User = {
    User(handDeck = Seq.empty, lifePoints = -1, name = "Der Tisch")
  }

  def returnController(): Controller = {
    val controller = Controller(updatedGameState)
    controller
  }
}
