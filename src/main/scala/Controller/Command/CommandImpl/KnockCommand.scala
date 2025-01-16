package Controller.Command.CommandImpl

import Controller.Controller
import Model._
import _root_.Controller.Command.Command

class KnockCommand(controller: Controller) extends Command {

  var memento: GameStateTrait = controller.gameState

  override def execute(): Unit = {
    memento = controller.gameState

    val updatedKnockCounter = controller.gameState.knockCounter + 1
    val updatedGameState = controller.gameState.copy(knockCounter = updatedKnockCounter,
      gameOver = updatedKnockCounter >= 2, queue = controller.gameState.queue + 1)
    
    controller.gameState = updatedGameState
  }

  override def undoStep(): Unit = {
    val memento = controller.gameState

    controller.gameState = this.memento

    this.memento = memento
  }

  override def redoStep(): Unit = {
    val memento = controller.gameState

    controller.gameState = this.memento

    this.memento = memento
  }
}
