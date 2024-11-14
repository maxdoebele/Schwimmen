package Controller

import Model.{Card, CardDeck, User}

object GameLogic
{
  def distributeCardsToUser(deck: CardDeck, user: User): (CardDeck, User) = {
    val (drawnCards, newDeck) = deck.remove3Cards()
    val updatedUser = user.addCards(drawnCards)
    (newDeck, updatedUser)
  }

  def swapCards(playerUser: User, tableUser: User): (User, User) = {

  }
}











}


