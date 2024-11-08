package View

import Model.Card
class TUI() {
  //List[String] represents a rendered Card
  def drawCard(card: Card): List[String] = {
    val suitSymbol = card.suit match {
      case "Herz" => "♥"
      case "Pik" => "♠"
      case "Karo" => "♦"
      case "Kreuz" => "♣"
      case _ => "?"
    }

    List(
      "+-------+",
      f"|     ${card.rank}%-2s|",
      f"|   $suitSymbol   |",
      f"| ${card.rank}%-2s    |",
      "+-------+"
    )
  }

}
