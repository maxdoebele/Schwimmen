package View

import scala.io.StdIn._
import Controller._
import Controller.GameBuilder.{BuildNewGame, BuildNewRound}

object Main {
  def main(args: Array[String]): Unit = {
    println("Willkommen bei Schwimmen :)")

    val namen = readLine("Bitte sag mir die Namen der Mitspieler: ").split(" ").toList
    val currentGameConroller = BuildNewGame(namen).returnController()

    GameManage.playGame(currentGameConroller)
    print("Das Spiel ist vorbei")
    
  }
}
