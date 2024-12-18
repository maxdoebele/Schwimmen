package Controller.util

import Controller.COR.LifePointsHandler
import Controller.Command.{KnockCommand, SkipCommand, TradeAllCommand, TradeOneCommand}
import Controller.GameBuilder.BuildNewRound

import scala.util.{Failure, Success, Try}
import Model.{Card, GameState, User}

import scala.util.Try

class Controller(var gameState: GameState) extends Observable {

  val undoManager: UndoManager = new UndoManager()

  @volatile var threadReadLine: Thread = new Thread()

  private def executeCommand(action: => Unit): Unit = {
    cancelReadLine()
    action
    if(checkifGameOver()) {
      resetRound()
    }
    notifySubscribers()
  }

  def cancelReadLine(): Unit = {
    if (threadReadLine != null && threadReadLine.isAlive) {
      threadReadLine.interrupt()
    }
  }

  def knock(): Unit = executeCommand {
    undoManager.execute(KnockCommand(this))
  }

  def skip(): Unit = executeCommand {
    undoManager.execute(SkipCommand(this))
  }

  def tradeAll(): Unit = executeCommand {
    undoManager.execute(TradeAllCommand(this))
  }

  def tradeOne(indexCardPlayer: Int, indexCardTable: Int): Unit = executeCommand {
    gameState = gameState.copy(indexCardPlayer = indexCardPlayer, indexCardTable = indexCardTable)
    undoManager.execute(TradeOneCommand(this))
  }

  def undo(): Unit = executeCommand {
    undoManager.undoStep()
  }

  def redo(): Unit = executeCommand {
    undoManager.redoStep()
  }

  def checkForSchnauz(controller: Controller): Unit = {
    val schnauzPlayer = controller.gameState.players.find(player => calculatePoints(player.handDeck).getOrElse(0.0) == 31)
    val feuerSchnautzPlayer = controller.gameState.players.find(player => calculatePoints(player.handDeck).getOrElse(0.0) == 33)

    if (schnauzPlayer.isDefined || feuerSchnautzPlayer.isDefined) {
      controller.gameState = controller.gameState.copy(gameOver = true)
    }
  }

  def calculatePoints(cards: Seq[Card]): Try[Double] = Try {
    val halbe = 30.5
    val schnauz = 31
    val feuer = 33

    if (cards.size != 3) throw new IllegalArgumentException("Genau drei Karten erwartet.")

    if (cards.map(_.rank).distinct.size == 1) {
      if (cards.head.rankToPoints * 3 == feuer)
        feuer
      else
        halbe
    } else {
      val groupedBySuit = cards.groupBy(_.suit)
      val sameSuitGroup = groupedBySuit.values.find(_.size > 1)

      sameSuitGroup match {
        case Some(sameSuitCards) =>
          sameSuitCards.map(_.rankToPoints).sum // Sum points of cards with the same suit
        case None =>
          cards.map(_.rankToPoints).max // All cards have different suits, return the highest rank points
      }
    }
  }

  def findLoserOfRound(allPlayers: Seq[User]): Seq[User] = {
    val usersPoints: Seq[(User, Try[Double])] = allPlayers.map(user => user -> calculatePoints(user.handDeck))
    val successfulPoints = usersPoints.collect { case (user, Success(points)) => (user, points) }

    if (successfulPoints.isEmpty) {
      return Seq.empty[User]
    }
    val minPoints = successfulPoints.map(_._2).min
    successfulPoints.collect {
      case (user, points) if points == minPoints => user
    }
  }

  def checkifGameOver(): Boolean = {
    checkForSchnauz(this)
    if (this.gameState.gameOver) {
      val losers = findLoserOfRound(this.gameState.players)
      this.gameState = this.gameState.copy(lastLoosers = losers)

      new LifePointsHandler().handle(this, losers)
      return true
    }

    false
  }


  def resetRound(): Unit = {
    this.gameState = BuildNewRound(this.gameState).returnGameState()
  }

}
