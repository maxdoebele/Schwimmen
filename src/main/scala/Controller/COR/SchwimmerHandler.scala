package Controller.COR

import Controller.util.Controller
import Model.User

class SchwimmerHandler extends Handler {
  override def handle(controller: Controller, potentialSwimmer: Seq[User]): Unit = {
    if (controller.gameState.schwimmer) {
    } else {
      controller.gameState.players.foreach { swimmer =>
        if (potentialSwimmer.exists(_.name == swimmer.name)) {
          swimmer.setSchwimmer()
          val updatedPlayer = swimmer.addLifePoint()
          val updatedPlayers = controller.gameState.players.map { p =>
            if (p.name == updatedPlayer.name) updatedPlayer else p
          }
          controller.gameState = controller.gameState.copy(players = updatedPlayers)
        }
      }
      controller.gameState = controller.gameState.copy(schwimmer = true)
    }
  }
}

