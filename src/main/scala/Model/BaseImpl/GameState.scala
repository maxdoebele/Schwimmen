package Model.BaseImpl

import Model.GameStateTrait

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
                      override val lastLoosers: Seq[User] = Seq.empty[User]
                    ) extends GameStateTrait
