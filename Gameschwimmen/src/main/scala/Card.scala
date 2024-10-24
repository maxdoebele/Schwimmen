class Card(val suit: String, val rank: String) {
  // Methode, um jede Zeile der Karte als Liste von Strings zurückzugeben
  def getCardLines(): List[String] = {
    val suitSymbol = suit match {
      case "Herz" => "♥"
      case "Pik" => "♠"
      case "Karo" => "♦"
      case "Kreuz" => "♣"
      case _ => "?"
    }

    List(
      "+-------+",
      f"|     $rank%-2s|",
      f"|   $suitSymbol   |",
      f"| $rank%-2s    |",
      "+-------+"
    )
  }
}
