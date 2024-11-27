package Controller

import Model._

trait playerActions {
  def playerAction(currentPlayer: User, gameState: GameState): GameState
}

