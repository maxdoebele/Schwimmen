package Model

case class GameState(
  players: Seq[User],
  table: User,
  deck: Seq[Card],
  round: Int = 0,
  knockCounter: Int = 0,
  gameOver: Boolean = false){

}
