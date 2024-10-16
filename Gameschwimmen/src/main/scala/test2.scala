import scala.io.StdIn._

class CardPrinter {

  def printCard(suit: String, rank: String): Unit = {
    // ASCII-Zeichen für die verschiedenen Suits
    val suitSymbol = suit match {
      case "Herz" => "♥"
      case "Pik"  => "♠"
      case "Karo" => "♦"
      case "Kreuz"=> "♣"
      case _      => "?"
    }

    println(s"+-------+")
    println(s"|   $rank$suitSymbol  |")
    println(s"|       |")
    println(s"|       |")
    println(s"+-------+")
  }
}
  object Main {
    def main(args: Array[String]): Unit = {
      println("Willkommen bei Schwimmen!")
      println("Bitte gib die Anzahl an Spielern ein und deren Namen ein.")

      val anzahl = readLine("Anzahl: ").toInt
      val namen = readLine("Namen (getrennt durch Leerzeichen): ").split(" ").toList

      val cardPrinter = new CardPrinter()

      println("Okay, fangen wir an. Jeder kriegt jetzt 3 Karten ;)")
      namen.foreach { name =>
        println(s"$name, das sind deine Karten:")
        cardPrinter.printCard("Herz", "8")
        cardPrinter.printCard("Kreuz", "A")
        cardPrinter.printCard("Karo", "J")
      }

    }
  }


