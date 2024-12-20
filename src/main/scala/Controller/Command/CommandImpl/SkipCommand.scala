package Controller.Command.CommandImpl

import Controller.Controller
import Model.BaseImpl.{GameState, User}
import Model.GameStateTrait
import _root_.Controller.Command.Command

class SkipCommand(controller: Controller) extends Command {
  var memento: GameStateTrait = controller.gameState

  override def execute(): Unit = {
    memento = controller.gameState;
    val updatedGameState = controller.gameState.copy(queue = controller.gameState.queue + 1)
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
