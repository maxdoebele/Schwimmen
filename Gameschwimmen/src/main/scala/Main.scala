import scala.io.StdIn._

object Main {
  def main(args: Array[String]): Unit = {
    println("Willkommen bei Schwimmen :)")

    val namen = readLine("Bitte sag mir die Namen der Mitspieler: ").split(" ").toList
    val carddeck = new CardDeck
    println("Alles Klar, die Karten werden nun gemischt und ausgeteilt!")
    carddeck.shuffleDeck()

    // für jeden spieler drei karten ausgeben
    namen.foreach { name =>
      println(s"$name, das sind deine Karten:")
      carddeck.drawThree()
    }

    // to do: karten nur verdeckt anzeigen bis sich einer entschließt sie aufzudecken
    println("Das sind die Karten in der Mitte:")
    carddeck.drawThree()
  }
}
