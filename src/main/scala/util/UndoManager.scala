package util

import Controller.Command.Command

class UndoManager {

  private var undoStack: List[Command] = Nil
  private var redoStack: List[Command] = Nil

  def execute(command: Command): Unit = {
    undoStack = command :: undoStack
    command.execute()
  }

  def undoStep(): Unit = {
    undoStack match {
      case Nil =>
      case head :: stack => {
        head.undoStep()
        undoStack = stack
        redoStack = head :: redoStack
      }
    }
  }

  def redoStep(): Unit = {
    redoStack match {
      case Nil =>
      case head :: stack => {
        head.redoStep()
        redoStack = stack
        undoStack = head :: undoStack
      }
    }
  }
}