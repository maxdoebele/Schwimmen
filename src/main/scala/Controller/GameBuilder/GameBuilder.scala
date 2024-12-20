package Controller.GameBuilder

import Controller.Controller
import Model.*
import Model.BaseImpl.{CardDeck, GameState, User}

trait GameBuilder {
  def createCardDeck(): CardDeck

  def updateTable(): User

  def returnGameState(): GameStateTrait

  def distributeCards(users: Seq[User], userTable: User, cardDeck: CardDeck): GameState = {
    val (deckAfterPlayers, usersWithCards) = users.foldLeft((cardDeck, Seq.empty[User])) {
      case ((currentDeck, updatedUsers), user) =>
        val (updatedDeck, updatedUser) = distributeCardsToUser(currentDeck, user)
        (updatedDeck, updatedUsers :+ updatedUser)
    }
    val (finalDeck, tableWithCards) = distributeCardsToUser(deckAfterPlayers, userTable)

    GameState(usersWithCards, tableWithCards, finalDeck)
  }

  //*************Helper Funktions*********************
  def distributeCardsToUser(deck: CardDeck, user: User): (CardDeck, User) = {
    val (drawnCards, newDeck) = deck.remove3Cards()
    if (user.handDeck.isEmpty) {
      val updatedUser = user.add3Cards(drawnCards)
      (newDeck, updatedUser)
    } else {
      val (noCardUser,_) = user.removeAllCards()
      val updatedUser = noCardUser.add3Cards(drawnCards)
      (newDeck, updatedUser)
    }
  }

}
