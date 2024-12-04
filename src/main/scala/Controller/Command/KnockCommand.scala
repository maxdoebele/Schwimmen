package Controller.Command
import Controller.util.Controller
import Model.*

class KnockCommand(controller: Controller) extends Command {

  var memento: GameState = controller.gameState

  override def execute(): Unit = {
    memento = controller.gameState

    val updatedKnockCounter = controller.gameState.knockCounter + 1
    val updatedGameState = controller.gameState.copy(knockCounter = updatedKnockCounter, gameOver = updatedKnockCounter >= 2)
    
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
