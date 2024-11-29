package Controller

import Model._

trait GameBuilder {
  def createCardDeck(): CardDeck

  def createPlayers(playersName: Seq[String]): Seq[User]

  def createTable(): User

  def distributeCards(users: Seq[User], userTable: User, cardDeck: CardDeck): GameState

  def returnGameState(): GameState

}
