package Model

import Model.BaseImpl.*
import play.api.libs.json.{Format, Json}

/**
 * Trait for the GameState
 * Implements methods that define the GameState
 * copy method to create a new GameState with updated values
 */

trait GameStateTrait {
  def players: Seq[User]
  def table: User
  def deck: CardDeck
  def queue: Int
  def knockCounter: Int
  def gameOver: Boolean
  def indexCardPlayer: Int
  def indexCardTable: Int
  def schwimmer: Boolean
  def roundCounter: Int
  def lastLoosers: Seq[User]
  def playerPoints: Seq[Double]
  
  def copy(
            players: Seq[User] = this.players,
            table: User = this.table,
            deck: CardDeck = this.deck,
            queue: Int = this.queue,
            knockCounter: Int = this.knockCounter,
            gameOver: Boolean = this.gameOver,
            indexCardPlayer: Int = this.indexCardPlayer,
            indexCardTable: Int = this.indexCardTable,
            schwimmer: Boolean = this.schwimmer,
            roundCounter: Int = this.roundCounter,
            lastLoosers: Seq[User] = this.lastLoosers,
            playerPoints: Seq[Double] = this.playerPoints
          ): GameStateTrait
}