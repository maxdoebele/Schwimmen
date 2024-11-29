package View

import scala.io.StdIn.*
import Controller.*
import Controller.GameBuilder.BuildGame

object Main {
  def main(args: Array[String]): Unit = {
    println("Willkommen bei Schwimmen :)")

    val namen = readLine("Bitte sag mir die Namen der Mitspieler: ").split(" ").toList
    val currentGame = BuildGame(namen).returnGameState()
    val tui = new TUI()
    tui.displayGameState(currentGame)

    val currentRound = GameManage.playGame(currentGame)
    val finishedRound = UpdateGameState.updateLivePoints(currentGame, GameManage.findLoser(currentRound.players))
    
    
  }
  
}
