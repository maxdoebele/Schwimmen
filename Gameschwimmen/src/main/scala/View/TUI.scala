package View

import Model.{Card, GameState, User}

class TUI {
  // Draws a single card as a List[String]
  def drawCard(card: Card): List[String] = {
    val suitSymbol = card.suit match {
      case "Herz"  => "♥"
      case "Pik"   => "♠"
      case "Karo"  => "♦"
      case "Kreuz" => "♣"
      case _       => "?"
    }

    List(
      "+-------+",
      f"|     ${card.rank}%-2s|",
      f"|   $suitSymbol   |",
      f"| ${card.rank}%-2s    |",
      "+-------+"
    )
  }

  // Display the full game state, with each player’s and the table’s hands
  def displayGameState(gameState: GameState): Unit = {
    gameState.players.foreach { player =>
      println(s"${player.name}'s hand:")
      displayHand(player.handDeck)
      println() // Line break between players
    }

    println("Table's hand:")
    displayHand(gameState.table.handDeck)
    println() // Line break after table hand
  }

  // Display a hand of cards, with cards shown side-by-side
  private def displayHand(handDeck: Seq[Card]): Unit = {
    // Map each card to its drawn representation
    val drawnCards = handDeck.map(drawCard)
    // Transpose to align each line across all cards
    val transposedLines = drawnCards.transpose
    // Print each row of the cards side-by-side
    transposedLines.foreach(row => println(row.mkString(" ")))
  }
}
