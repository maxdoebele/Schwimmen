package Controller.GameBuilder

import Controller.util.Controller
import Model._

class BuildNewRound(gameState: GameState) extends GameBuilder {

  private val cardDeck: CardDeck = createCardDeck()
  private val players: Seq[User] = updatePlayers(gameState.players)
  private val table: User = updateTable()
  private val updatedGameState: GameState = distributeCards(players, table, cardDeck)
  
  override def createCardDeck(): CardDeck = {
    new CardDeck().shuffleDeck()
  }

  def updatePlayers(players: Seq[User]): Seq[User] = {
    if (players.nonEmpty) {
      val filteredPlayers = players.filter(user => user.livePoints > 0)
      filteredPlayers.tail :+ filteredPlayers.head
    } else {
      players
    }
  }


  override def updateTable(): User = {
    User(handDeck = Seq.empty, livePoints = -1, name = "Der Tisch")
  }

  def returnController(): Controller = {
    val controller = Controller(updatedGameState)
    controller
  }
}
