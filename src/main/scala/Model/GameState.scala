package Model

case class GameState(
  players: Seq[User],
  table: User,
  deck: CardDeck,
  queue: Int = 0,
  knockCounter: Int = 0,
  gameOver: Boolean = false, 
  indexCardPlayer: Int = 0,
  indexCardTable: Int = 0)
