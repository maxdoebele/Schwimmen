package Controller.GameBuilder.GameBuilderImpl

import Model._
import Model.BaseImpl.{CardDeck, GameState, User}
import _root_.Controller.GameBuilder.GameBuilder

class BuildNewRound(gameState: GameStateTrait) extends GameBuilder {
  
  private val cardDeck: CardDeck = createCardDeck()
  private val players: Seq[User] = updatePlayers(gameState.players)
  private val table: User = updateTable()
  private val schwimmer: Boolean = gameState.schwimmer
  private val distibuteCardGameState: GameState = distributeCards(players, table, cardDeck)
  private val updatedGameState: GameStateTrait = mergeNewGameState()

  private def mergeNewGameState(): GameStateTrait = {
    val updated = gameState.copy(players = distibuteCardGameState.players, 
      table = distibuteCardGameState.table, 
      deck = distibuteCardGameState.deck, 
      queue = 0, 
      knockCounter = 0, 
      gameOver = false,
      indexCardPlayer = 0, 
      indexCardTable = 0, 
      schwimmer = gameState.schwimmer,
      roundCounter = gameState.roundCounter + 1)
    updated
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

  override def returnGameState(): GameStateTrait = {
    updatedGameState
  }
}
