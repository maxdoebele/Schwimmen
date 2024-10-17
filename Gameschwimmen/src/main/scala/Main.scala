import scala.io.StdIn._

  object Main {
    def main(args: Array[String]): Unit = {
      println("Willkommen bei Schwimmen!")

      val namen = readLine("Bitte sag mir die Namen der Mitspieler: ").split(" ").toList
      val carddeck = new CardDeck
      println("Alles Klar, die Karten werden gemischt!")
      carddeck.shuffleDeck()
      namen.foreach { name =>
        println(s"$name, das sind deine Karten:")
        for (i <- 1 to 3) { // Die Schleife läuft von 1 bis 3
          val card = carddeck.drawCard() // Karte ziehen
          card.printCard() // Zeige die gezogene Karte an
        }
        println()
      }

    }
  }


