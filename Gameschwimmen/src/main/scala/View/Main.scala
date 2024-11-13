package View

import scala.io.StdIn._
import Model._
import Controller._

object Main {
  def main(args: Array[String]): Unit = {
    println("Willkommen bei Schwimmen :)")

    // Namen der Spieler eingeben und Kartendeck initialisieren
    val namen = readLine("Bitte sag mir die Namen der Mitspieler: ").split(" ").toList
    var carddeck = new CardDeck().shuffleDeck()
    println("Alles Klar, die Karten werden nun gemischt und ausgeteilt!")

    var spielEnde = false
    var klopfZaehler = 0

    while (!spielEnde) {
      namen.foreach { spieler =>
        println(s"\n$spieler, du bist dran!")

        // Aktionen anzeigen
        println("Was möchtest du tun?")
        println("1. Karte tauschen")
        println("2. Schieben")
        println("3. Klopfen")

        val aktion = readLine("Gib die Nummer deiner Aktion ein: ")

        aktion match {
          case "1" =>
          // irgendwas mit handdeck und mittel deck ???
          case "2" =>
            println(s"$spieler hat geschoben.")
          case "3" =>
            klopfZaehler += 1
            println(s"$spieler hat geklopft.")
            if (klopfZaehler == 2) {
              spielEnde = true
              println("Zwei Spieler haben geklopft. Alle Karten auf den Tisch!.")
            }
          case _ =>
            println("Ungültige Eingabe.")
        }
        if (spielEnde) return
      }
    }
  }
}
