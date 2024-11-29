package Controller
import Model.{CardDeck, GameState, User}

case class BuildGame(playerNames: Seq[String]) extends GameBuilder {

  private val cardDeck: CardDeck = createCardDeck()
  private val players: Seq[User] = createPlayers(playerNames)
  private val table: User = createTable()
  private val gameState: GameState = distributeCards(players, table, cardDeck)

  override def returnGameState(): GameState = {
    gameState
  }
  override def createCardDeck(): CardDeck = {
    new CardDeck().shuffleDeck()
  }
  override def createPlayers(playersName: Seq[String]): Seq[User] = {
    val users: Seq[User] = playersName.map { name =>
      User(handDeck = Seq.empty, livePoints = 3, name = name)
    }
    users
  }
  override def createTable(): User = {
    User(handDeck = Seq.empty, livePoints = -1, name = "Der Tisch")
  }
  override def distributeCards(users: Seq[User], userTable: User, cardDeck: CardDeck): GameState = {
    val (deckAfterPlayers, usersWithCards) = users.foldLeft((cardDeck, Seq.empty[User])) {
      case ((currentDeck, updatedUsers), user) =>
        val (updatedDeck, updatedUser) = GameLogic.distributeCardsToUser(currentDeck, user)
        (updatedDeck, updatedUsers :+ updatedUser)
    }
    val (finalDeck, tableWithCards) = GameLogic.distributeCardsToUser(deckAfterPlayers, userTable)

    GameState(usersWithCards, tableWithCards, finalDeck, 1)
  }
}