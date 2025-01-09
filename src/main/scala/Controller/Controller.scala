package Controller

import Controller.COR.CORImpl.LifePointsHandler
import Controller.Command.CommandImpl.{KnockCommand, SkipCommand, TradeAllCommand, TradeOneCommand}
import Controller.GameBuilder.GameBuilderImpl.{BuildNewGame, BuildNewRound}
import Model._
import util.{Observable, UndoManager}

import scala.io.StdIn.readLine

class Controller(var gameState: GameStateTrait) extends Observable {
  
  val undoManager: UndoManager = new UndoManager()

  @volatile var threadReadLine: Thread = new Thread()

  private def executeCommand(action: => Unit): Unit = {
    //cancelReadLine()
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
    val schnauzPlayer = controller.gameState.players.find(player => HelpFunctions.calculatePoints(player.handDeck).getOrElse(0.0) == 31)
    val feuerSchnautzPlayer = controller.gameState.players.find(player => HelpFunctions.calculatePoints(player.handDeck).getOrElse(0.0) == 33)

    if (schnauzPlayer.isDefined || feuerSchnautzPlayer.isDefined) {
      controller.gameState = controller.gameState.copy(gameOver = true)
    }
  }


  def checkifGameOver(): Boolean = {
    checkForSchnauz(this)
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
    this.gameState = BuildNewGame(names).returnGameState()
    notifySubscribers()
  }
}
