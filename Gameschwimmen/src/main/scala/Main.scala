import scala.io.StdIn._

object Main {
  def main(args: Array[String]): Unit = {
    println("Willkommen bei Schwimmen :)")

    val namen = readLine("Bitte sag mir die Namen der Mitspieler: ").split(" ").toList
    val carddeck = new CardDeck
    println("Alles Klar, die Karten werden nun gemischt und ausgeteilt!")
    carddeck.shuffleDeck()

  }
}