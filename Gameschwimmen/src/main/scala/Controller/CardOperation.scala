package Controller

import Model.{Card, CardDeck, GameState, User}

class CardOperation(val suit: String, val rank: String) {

  def initializeNewGame(playersName: Seq[String]): GameState = {
    val cardDeck = new CardDeck().shuffleDeck()

    // Erstellen der Spieler (Users) mit ihren Handkarten und Leben
    val users: Seq[User] = playersName.map { name =>
      User(handDeck = Seq.empty, livePoints = 3, name = name)
    }

    val userTable = User(handDeck = Seq.empty, livePoints = -1, name = "TheTable")

    val (deckAfterPlayers, usersWithCards) = users.foldLeft((cardDeck, Seq.empty[User])) {
      case ((currentDeck, updatedUsers), user) =>
        val (updatedDeck, updatedUser) = GameLogic.distributeCardsToUser(currentDeck, user)
        (updatedDeck, updatedUsers :+ updatedUser)
    }
    val (finalDeck, tableWithCards) = GameLogic.distributeCardsToUser(deckAfterPlayers, userTable)

    GameState(usersWithCards, tableWithCards, finalDeck, 1)
  }


}
