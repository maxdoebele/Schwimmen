package Controller.COR
import Controller.util.Controller
import Model.User

class PotentialSchwimmerHandler extends Handler {
  override def handle(controller: Controller, loosers: Seq[User]): Unit = {
    controller.gameState.players.foreach { player =>
      if (loosers.exists(_.name == player.name)) {
        val potentialSwimmer = controller.gameState.players.filter { p =>
          p.lifePoints == 0 }
        if (potentialSwimmer.nonEmpty) {
          new SchwimmerHandler().handle(controller, potentialSwimmer)
        }
      }
    }
  }
}

