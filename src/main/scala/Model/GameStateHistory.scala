package  Model

object GameStateHistory {
  private var history: List[GameState] = List.empty

  def addGameState(gameState: GameState): Unit = {
    history = gameState :: history
  }

  def getHistory: List[GameState] = history

  def undoLastState(): Option[GameState] = {
    history match {
      case Nil => None
      case head :: tail =>
        history = tail
        Some(head)
    }
  }

}
