package View

import scala.io.StdIn._
import Model._
import Controller._

object Main {
  def main(args: Array[String]): Unit = {
    println("Willkommen bei Schwimmen :)")

    // Namen der Spieler eingeben und Kartendeck initialisieren
    val namen = readLine("Bitte sag mir die Namen der Mitspieler: ").split(" ").toList
    val carddeck = new CardDeck().shuffleDeck(carddeck)
    println("Alles Klar, die Karten werden nun gemischt und ausgeteilt!")

    while (!spielEnde) {
      namen.foreach { spieler =>
        println(s"\n$spieler, du bist dran!")

        println("Was möchtest du tun?")
        println("1. Karte tauschen")
        println("2. Schieben")
        println("3. Klopfen")
        // spieler aktionen aus controller
      }
    }
  }
}
