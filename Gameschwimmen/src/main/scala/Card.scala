class Card(val suit: String, val rank: String){
  def printCard(): Unit = {
    // ASCII-Zeichen für die verschiedenen Suits
    val suitSymbol = suit match {
      case "Herz" => "♥"
      case "Pik" => "♠"
      case "Karo" => "♦"
      case "Kreuz" => "♣"
      case _ => "?"
    }

    println(s"+-------+")
    println(s"|   $rank$suitSymbol  |")
    println(s"|       |")
    println(s"|       |")
    println(s"+-------+")
  }
}
