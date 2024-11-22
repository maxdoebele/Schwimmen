package Model

case class GameState(
  players: Seq[User],
  table: User,
  deck: CardDeck,
  queue: Int = 0,
  knockCounter: Int = 0,
  gameOver: Boolean = false){

}
