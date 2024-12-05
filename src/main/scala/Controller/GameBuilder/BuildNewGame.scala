package Controller.GameBuilder

import Controller.HelpFunctions
import Model._
import Controller.util.Controller

case class BuildNewGame(playerNames: Seq[String]) extends GameBuilder {

  private val cardDeck: CardDeck = createCardDeck()
  private val players: Seq[User] = createPlayers(playerNames)
  private val table: User = updateTable()
  private val gameState: GameState = distributeCards(players, table, cardDeck)
  
  override def createCardDeck(): CardDeck = {
    new CardDeck().shuffleDeck()
  }
  def createPlayers(playersName: Seq[String]): Seq[User] = {
    val users: Seq[User] = playersName.map { name =>
      User(handDeck = Seq.empty, livePoints = 3, name = name)
    }
    users
  }
  override def updateTable(): User = {
    User(handDeck = Seq.empty, livePoints = -1, name = "Der Tisch")
  }

  override def returnController(): Controller = {
    val controller = Controller(gameState)
    controller
  }
}