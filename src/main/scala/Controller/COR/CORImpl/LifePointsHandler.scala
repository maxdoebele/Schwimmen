package Controller.COR.CORImpl

import _root_.Controller.COR.CORImpl._
import Controller.COR.Handler
import Controller.Controller
import Model.BaseImpl.User

class LifePointsHandler extends Handler {

  override def handle(controller: Controller, loosers: Seq[User]): Unit = {
    controller.gameState.players.foreach { player =>
      if (loosers.exists(_.name == player.name)) {
        val updatedPlayer = player.loseLifePoint() // Spieler aktualisieren
        val updatedPlayers = controller.gameState.players.map { p =>
          if (p.name == updatedPlayer.name) updatedPlayer else p
        }
        controller.gameState = controller.gameState.copy(players = updatedPlayers) // GameState aktualisieren
        new PotentialSchwimmerHandler().handle(controller, loosers) // Weiterverarbeitung
      }
    }
  }
}
