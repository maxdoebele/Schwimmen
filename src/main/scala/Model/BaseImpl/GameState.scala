package Model.BaseImpl

import Model.GameStateTrait
import play.api.libs.json.{Format, Json, Reads, Writes}
import scala.xml.Elem

case class GameState(
                      override val players: Seq[User],
                      override val table: User,
                      override val deck: CardDeck,
                      override val queue: Int = 0,
                      override val knockCounter: Int = 0,
                      override val gameOver: Boolean = false,
                      override val indexCardPlayer: Int = 0,
                      override val indexCardTable: Int = 0,
                      override val schwimmer: Boolean = false,
                      override val roundCounter: Int = 0,
                      override val lastLoosers: Seq[User] = Seq.empty[User],
                      override val playerPoints: Seq[Double] = Seq.empty[Double]
                    ) extends GameStateTrait {
  def toXML: Elem = <gameState>
    <players>{this.players.map(player => player.toXML)}</players>
    <table>{this.table.toXML}</table>
    <deck>{this.deck.toXML()}</deck>
    <queue>{this.queue}</queue>
    <knockCounter>{this.knockCounter}</knockCounter>
    <gameOver>{this.gameOver}</gameOver>
    <indexCardPlayer>{this.indexCardPlayer}</indexCardPlayer>
    <indexCardTable>{this.indexCardTable}</indexCardTable>
    <swimmerGlobal>{this.schwimmer}</swimmerGlobal>
    <roundCounter>{this.roundCounter}</roundCounter>
    <lastLooser>{this.lastLoosers.map(looser=>looser.toXML)}</lastLooser>
    <playerPoints>{this.playerPoints}</playerPoints>
  </gameState>
}


object GameState {
  implicit val gameStateFormat: Format[GameState] = Json.format[GameState]

  def fromXML(xml: scala.xml.Node): GameState = {
    val players = (xml \ "players" \ "player").map(node => User.fromXML(node)).toList
    val table = User.fromXML((xml \ "table" \ "player").head)
    val deck = CardDeck.fromXML((xml \ "deck").head)
    val queue = (xml \ "queue").text.trim.toInt
    val knockCounter = (xml \ "knockCounter").text.trim.toInt
    val gameOver = (xml \ "gameOver").text.trim.toBoolean
    val indexCardPlayer = (xml \ "indexCardPlayer").text.trim.toInt
    val indexCardTable = (xml \ "indexCardTable").text.trim.toInt
    val swimmer = (xml \ "swimmerGlobal").text.trim.toBoolean
    val roundCounter = (xml \ "roundCounter").text.trim.toInt
    val lastLoosers = (xml \ "lastLooser" \ "looser").map(node => User.fromXML(node)).toList
    val playerPoints = (xml \ "playerPoints").text.trim.split(",").map(_.toDouble).toList

    // Reconstruct the GameState
    GameState(
      players = players,
      table = table,
      deck = deck,
      queue = queue,
      knockCounter = knockCounter,
      gameOver = gameOver,
      indexCardPlayer = indexCardPlayer,
      indexCardTable = indexCardTable,
      schwimmer = swimmer,
      roundCounter = roundCounter,
      lastLoosers = lastLoosers,
      playerPoints = playerPoints
    )
  }
}

  
