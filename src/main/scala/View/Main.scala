package View

import scala.io.StdIn.*
import Controller.*
import Controller.GameBuilder.{BuildNewGame, BuildNewRound}

object Main {
  def main(args: Array[String]): Unit = {
    println("Willkommen bei Schwimmen :)")

    val namen = readLine("Bitte sag mir die Namen der Mitspieler: ").split(" ").toList
    val currentGame = BuildNewGame(namen).returnGameState()

    GameManage.playGame(currentGame)
    print("Das Spiel ist vorbei")
    
  }
  
}
