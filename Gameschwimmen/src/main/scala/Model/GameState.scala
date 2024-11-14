package Model

case class GameState(players: Seq[User],table: User, deck: CardDeck, round: Int) {
  var knockCounter = 0;
  var gameOver = false

  def knockCount(): Unit = {
    knockCounter += 1
    if (knockCounter >= 2) {
      gameOver = true
    }
  }

}
