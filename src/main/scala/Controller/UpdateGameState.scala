package Controller

import Model._

object UpdateGameState {

  def updateGameState(
                       gameState: GameState,
                       currentPlayer: Option[User] = None,
                       allPlayers: Option[User] = None,
                       table: Option[User] = None,
                       queue: Option[Int] = None,
                       knockCounter: Option[Int] = None,
                       gameOver: Option[Boolean] = None
                     ): GameState = {
    val updatedPlayers = allPlayers match {
      case Some(user) =>
        gameState.players.map { player =>
          if (currentPlayer.exists(_.name == player.name)) user else player
        }
      case None => gameState.players
    }

    gameState.copy(
      players = updatedPlayers,
      table = table.getOrElse(gameState.table),
      queue = queue.getOrElse(gameState.queue),
      knockCounter = knockCounter.getOrElse(gameState.knockCounter),
      gameOver = gameOver.getOrElse(gameState.gameOver)
    )
  }

  def updateLivePoints(gameState: GameState, losers: Seq[User]): GameState = {
    val updatedPlayers = gameState.players.map { player =>
      if (losers.exists(_.name == player.name)) {
        player.loseLivePoint()
      } else {
        player
      }
    }

    gameState.copy(players = updatedPlayers)
  }

}