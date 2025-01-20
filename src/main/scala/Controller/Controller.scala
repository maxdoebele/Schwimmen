package Controller

import COR.CORImpl.LifePointsHandler
import Command.CommandImpl.{KnockCommand, SkipCommand, TradeAllCommand, TradeOneCommand}
import FileIO.FileIO
import GameBuilder.GameBuilder
import GameBuilder.GameBuilderImpl.{BuildNewGame, BuildNewRound}
import Model._
import Model.BaseImpl.GameState
import util.{Observable, UndoManager}
import com.google.inject.Inject

class Controller @Inject() (var gameBuilder : GameBuilder, val fileIO: FileIO) extends Observable {
  
  val undoManager: UndoManager = new UndoManager()
  var gameState: GameStateTrait = gameBuilder.returnGameState()

  @volatile var threadReadLine: Thread = new Thread()

  private def executeCommand(action: => Unit): Unit = {
    action
    if(checkifGameOver()) {
      this.gameState = HelpFunctions.getPlayerPoints(this)
      resetRound()
    }
    notifySubscribers()
  }

  def undo(): Unit = executeCommand {
    undoManager.undoStep()
  }

  def redo(): Unit = executeCommand {
    undoManager.redoStep()
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

  def checkForSchnauz(): Unit = {
    val schnauzPlayer = this.gameState.players.find(player => HelpFunctions.calculatePoints(player.handDeck).getOrElse(0.0) == 31)
    val feuerSchnauzPlayer = this.gameState.players.find(player => HelpFunctions.calculatePoints(player.handDeck).getOrElse(0.0) == 33)

    if (schnauzPlayer.isDefined || feuerSchnauzPlayer.isDefined) {
      this.gameState = this.gameState.copy(gameOver = true)
    }
  }


  def checkifGameOver(): Boolean = {
    checkForSchnauz()
    someOneKnocked()
    if (this.gameState.gameOver) {
      val losers = HelpFunctions.findLoserOfRound(this.gameState.players)
      this.gameState = this.gameState.copy(lastLoosers = losers)

      new LifePointsHandler().handle(this, losers)
      return true
    }
    false
  }

  def resetRound(): Unit = {
    this.gameState = BuildNewRound(this.gameState).returnGameState()
  }

  def createNewGame(names: Seq[String]): Unit = {
    gameState = BuildNewGame(names).returnGameState()
    notifySubscribers()
  }

  def loadGame(): Unit = {
    this.gameState = fileIO.readFile()
    this.notifySubscribers()
  }

  def saveGame(): Unit = {
    fileIO.createFile(this.gameState.asInstanceOf[GameState])
    this.notifySubscribers()
  }

  private def someOneKnocked(): Unit = {
    val player1 = HelpFunctions.getCurrentPlayer(this.gameState)
    val player2 = this.gameState.players.find(_.knocked)

    player2 match {
      case Some(knockedPlayer) =>
        if (player1 == knockedPlayer) {
          this.gameState = this.gameState.copy(gameOver = true)
        }
      case None =>
    }
  }
}
