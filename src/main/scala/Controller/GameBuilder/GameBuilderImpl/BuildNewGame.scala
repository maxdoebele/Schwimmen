package Controller.GameBuilder.GameBuilderImpl

import Model._
import Model.BaseImpl.{CardDeck, GameState, User}
import _root_.Controller.GameBuilder.GameBuilder
import com.google.inject.Inject
import com.google.inject.name.Named

case class BuildNewGame @Inject() (@Named("playerNames") playerNames: Seq[String]) extends GameBuilder {

  private val cardDeck: CardDeck = createCardDeck()
  private val players: Seq[User] = createPlayers(playerNames)
  private val table: User = updateTable()
  private val gameState: GameState = distributeCards(players, table, cardDeck)
  
  override def createCardDeck(): CardDeck = {
    new CardDeck().shuffleDeck()
  }

  def createPlayers(playersName: Seq[String]): Seq[User] = {
    val users: Seq[User] = playersName.map { name =>
      User(handDeck = Seq.empty, lifePoints = 3, name = name)
    }
    users
  }
  override def updateTable(): User = {
    User(handDeck = Seq.empty, lifePoints = -1, name = "Der Tisch")
  }

  override def returnGameState(): GameState = {
    gameState
  }
}