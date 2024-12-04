package Controller.util

import Controller.Command.{KnockCommand, SkipCommand, TradeAllCommand, TradeOneCommand}
import Model.GameState

class Controller(var gameState: GameState) extends Observable {
  
  val undoManager: UndoManager = new UndoManager()
  
  def knock(): Unit = {
    undoManager.execute(KnockCommand(this))
    
    notifySubscribers()
  }

  def skip(): Unit = {
    undoManager.execute(SkipCommand(this))

    notifySubscribers()
  }

  def tradeAll(): Unit = {
    undoManager.execute(TradeAllCommand(this))

    notifySubscribers()
  }

  def tradeOne(indexCardPlayer: Int, indexCardTable: Int): Unit = {
    val newGameState = gameState.copy(indexCardTable = indexCardTable, indexCardPlayer = indexCardPlayer)
    undoManager.execute(TradeOneCommand(this))

    notifySubscribers()
  }
  
  def undo(): Unit = {
    undoManager.undoStep()
    
    notifySubscribers()
  }
  
  def redo(): Unit = {
    undoManager.redoStep()
    
    notifySubscribers()
  }
}
