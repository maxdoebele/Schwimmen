package View.gui

import FileIO.FileIO
import Model.BaseImpl.{Card, GameState}
import _root_.Controller.{Controller, HelpFunctions}
import _root_.Controller.HelpFunctions.checkForPlayerLimit
import com.google.inject.Inject
import javafx.geometry._
import scalafx.Includes._
import scalafx.application.{JFXApp3, Platform}
import scalafx.beans.binding.Bindings
import scalafx.beans.property.BooleanProperty
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.{Button, ComboBox, Label, TextField}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{Border, BorderStroke, BorderStrokeStyle, BorderWidths, CornerRadii, GridPane, HBox, Pane, Region, StackPane, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.paint.Color._
import scalafx.scene.text.{Text, TextAlignment}
import scalafx.scene.layout.{HBox, Region, StackPane, VBox}
import scalafx.scene.paint.Color._
import scalafx.scene.text.Text
import util.Observer
import java.io.File
import java.net.URL
import java.util.concurrent.atomic.AtomicInteger
import scala.jdk.CollectionConverters._
import scala.collection.IterableOnce.iterableOnceExtensionMethods
import scala.jdk.CollectionConverters._
import scala.sys.process.buildersToProcess
import scala.util.{Failure, Success, Try}

class GUI @Inject() (val controller: Controller ) extends JFXApp3 with Observer {

  controller.add(this)

  private val folderPath = "Karten/"
  private val cardFileMap = loadCardDeck(folderPath)

  private var cardIndex = (-1, -1)
  private var cardIndexValid = BooleanProperty(false)
  private var lastSelectedButtonTable: Option[Button] = None
  private var lastSelectedButtonUser: Option[Button] = None

  private var firstRoundKnockValid = BooleanProperty(false)
  private var playerLimit = BooleanProperty(false)
  private val roundCounter: AtomicInteger = new AtomicInteger(1)


  override def update(): Unit = {
    Platform.runLater {
      firstRoundKnockValid.value = controller.gameState.queue < controller.gameState.players.size
      if (controller.gameState.players.size <= 1) {
        stage.scene = guiEndOfGameScene()
      } else if (controller.gameState.roundCounter == roundCounter.get()) {
        stage.scene = guiEndOfRoundScene()
        this.synchronized {
          roundCounter.incrementAndGet()
        }
      } else {
        stage.scene = guiupdatescene()
      }
    }
  }

  private def guistartscene(): Scene = {
    var enteredNames: Seq[String] = Seq.empty
    val Schwimmen = new Image("file:src/main/resources/Logo.png")
    val playerNamesText = new Text {
      textAlignment = TextAlignment.Center
      wrappingWidth = 250
      style = Style.defaultTextWhite
      text = "Spieler: "
    }
    val playerNames = new TextField {
      text = "Spielernamen eingeben"
      style = Style.defaultTextWhite
      maxWidth = 250
      maxHeight = 30
      onAction = _ => {
        val name = text.value.trim
        if (name.nonEmpty) {
          if (enteredNames.size < 9) {
            enteredNames = enteredNames :+ name
            playerLimit.value = checkForPlayerLimit(enteredNames)
            playerNamesText.text = s"Spieler:\n ${enteredNames.mkString("\n")}"
            text = ""
          } else {
            playerNamesText.text = "Zu viele Spieler! Bitte erneut eingeben."
            enteredNames = Seq.empty
            playerLimit.value = false
            text = ""
          }
        }
      }
    }
    val welcome = new Label {
      text = "Willkommen bei"
      style = Style.boldTextWhite
    }
    val logo = new ImageView(Schwimmen) {
      fitWidth = 200
      preserveRatio = true
    }
    val startButton = new Button {
      minWidth = 150
      minHeight = 46.82
      style <== when(disable) choose Style.transparentButton otherwise Style.startButton
      disable <== playerLimit.not
      onAction = _ => {
        controller.createNewGame(enteredNames)
      }
    }

    new Scene(700, 500) {
      root = new Pane {
        style = Style.BackgroundStart
        children = Seq(
          welcome,
          logo,
          playerNames,
          playerNamesText,
          startButton
        )
        welcome.layoutX = 300
        welcome.layoutY = 115
        logo.layoutX = 250
        logo.layoutY = 138
        playerNames.layoutX = 265
        playerNames.layoutY = 180
        playerNamesText.layoutX = 225
        playerNamesText.layoutY = 230
        startButton.layoutX = 280
        startButton.layoutY = 375
      }
    }
  }


  private def guiupdatescene(): Scene = {
    val loadButton = new Button {
      text = "load"
      style = Style.buttonStyle
      onAction = _ => {
        loadGame()
      }
    }
    val saveButton = new Button {
      text = "save"
      style = Style.buttonStyle
      onAction = _ => {
        saveGame()
      }
    }
    val tableCardsLabel = new Label {
      text = "Table Cards"
      style = Style.boldText
    }
    val tableCards = new HBox {
      alignment = Pos.CENTER
      children = createCardDisplayTable(controller.gameState.table.handDeck)
    }
    val playerCardsLabel = new Label {
      text = s"${HelpFunctions.getCurrentPlayer(controller.gameState).name}'s Cards"
      style = Style.boldText
    }
    val playerCards = new HBox {
      alignment = Pos.CENTER
      spacing = 10
      children = createCardDisplayUser(HelpFunctions.getCurrentPlayer(controller.gameState).handDeck)
    }
    val playButtons = new HBox {
      alignment = Pos.CENTER
      spacing = 20
      children = Seq(
        createKnockButton,
        createButton("Skip", controller.skip()),
        createButton("Trade ALL", controller.tradeAll()),
        createTradeOneButton
      )
    }
    new Scene(700, 500) {
      root = new Pane {
        stage.resizable = false
        style = Style.BackgroundUpdate
        children = Seq(
          loadButton,
          saveButton,
          tableCardsLabel,
          tableCards,
          playerCardsLabel,
          playerCards,
          playButtons
        )
        loadButton.layoutX = 10
        loadButton.layoutY = 20
        saveButton.layoutX = 620
        saveButton.layoutY = 20
        tableCardsLabel.layoutX = 315
        tableCardsLabel.layoutY = 20
        tableCards.layoutX = 165
        tableCards.layoutY = 50
        playerCardsLabel.layoutX = 315
        playerCardsLabel.layoutY = 230
        playerCards.layoutX = 165
        playerCards.layoutY = 260
        playButtons.layoutX = 120
        playButtons.layoutY = 430
      }
    }
  }

  private def guiEndOfRoundScene(): Scene = {
    val schwimmer = controller.gameState.players.find(_.swimming)
    val losers = controller.gameState.lastLoosers.map(_.name).mkString("\n")
    val currentScores = HelpFunctions.calculateCurrentScore(controller)
    val weiterButton = new Button {
      minWidth = 150
      minHeight = 46.82
      style = Style.weiterButton
      onAction = _ => {
        stage.scene = guiupdatescene()
      }
    }
    val scoreGrid = new GridPane {
      hgap = 40
      vgap = 5
      padding = Insets(10)

      currentScores.zipWithIndex.foreach { case ((name, score), index) =>
        val nameLabel = new Label {
          text = name
          alignment = Pos.CENTER_LEFT
          style = Style.boldTextWhite
        }
        val scoreLabel = new Label {
          text = score.toString
          alignment = Pos.CENTER_RIGHT
          style = Style.boldTextWhite
        }
        add(nameLabel, 0, index)
        add(scoreLabel, 1, index)
      }
    }
    val currentScoresText = new Label {
      text = "Aktueller Punktestand"
      style = Style.bigBoldTextWhite
    }
    val losersText = new Label {
      text = s"${losers}"
      style = Style.boldText
    }
    val schwimmerText = new Label {
      schwimmer match {
        case Some(player) =>
          text = s"${player.name}"
          style = Style.boldText
        case None         =>
          text = "Keiner"
          style = Style.boldText
      }
    }
    new Scene(700, 500) {
      root = new Pane {
        style = Style.BackgroundEnd
        children = Seq(
          currentScoresText,
          scoreGrid,
          losersText,
          schwimmerText,
          weiterButton
        )
        losersText.layoutX = 50
        losersText.layoutY = 57
        currentScoresText.layoutX = 280
        currentScoresText.layoutY = 122
        scoreGrid.layoutX = 280
        scoreGrid.layoutY = 137
        schwimmerText.layoutX = 560
        schwimmerText.layoutY = 400
        weiterButton.layoutX = 270
        weiterButton.layoutY = 360
      }
    }
  }

  private def guiEndOfGameScene(): Scene = {
    val winner = controller.gameState.players.map(_.name).mkString("\n")
    val winnerText = new Label {
      text = s"${winner}"
      style = Style.bigBoldTextWhite
    }
    new Scene(700,500) {
      root = new Pane {
        style = Style.BackgroundEndGame
        children = Seq(
          winnerText
        )
        winnerText.layoutX = 330
        winnerText.layoutY = 230
      }
    }
  }

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title.value = "Game of Schwimmen"
      scene = guistartscene()
    }
  }

  // ----------------------------- Helper functions --------------------------------

  def createCardDisplayTable(handDeck: Seq[Card]): HBox = {
    new HBox {
      spacing = 10
      alignment = Pos.CENTER
      children = handDeck.map(createCardButtonTable)
    }
  }

  def createCardButtonTable(card: Card): Button = {
    val cardImagePath = cardFileMap.getOrElse(card, "default.png")

    new Button {
      graphic = new ImageView {
        image = new Image(getClass.getClassLoader.getResourceAsStream(s"$folderPath$cardImagePath"))
        fitWidth = 100
        preserveRatio = true
      }
      onAction = _ => {
        val table = controller.gameState.table
        val cardIdxTry = Try(table.handDeck.indexOf(card))
        cardIdxTry match {
          case Success(cardIdx) =>
            cardIndex = (cardIndex._1, cardIdx)
            cardIndexValid.value = cardIndex._1 != -1 && cardIndex._2 != -1
            lastSelectedButtonTable.foreach(_.style = "-fx-background-color: transparent;")
            this.style = Style.BorderBlack
            lastSelectedButtonTable = Some(this)
          case Failure(exception) =>
            println(s"Error finding card index: ${exception.getMessage}")
        }
      }
      style = "-fx-background-color: transparent;"
    }
  }

  def createCardDisplayUser(handDeck: Seq[Card]): HBox = {
    new HBox {
      spacing = 10
      alignment = Pos.CENTER
      children = handDeck.map(createCardButtonUser)
    }
  }

  def createCardButtonUser(card: Card): Button = {
    val cardImagePath = cardFileMap.getOrElse(card, "default.png")

    new Button {
      graphic = new ImageView {
        image = new Image(getClass.getClassLoader.getResourceAsStream(s"$folderPath$cardImagePath"))
        fitWidth = 100
        preserveRatio = true
      }
      onAction = _ => {
        val currentPlayer = HelpFunctions.getCurrentPlayer(controller.gameState)
        val cardIdxTry = Try(currentPlayer.handDeck.indexOf(card))
        cardIdxTry match {
          case Success(cardIdx) =>
            cardIndex = (cardIdx, cardIndex._2)
            cardIndexValid.value = cardIndex._1 != -1 && cardIndex._2 != -1
            lastSelectedButtonUser.foreach(_.style = "-fx-background-color: transparent;")
            this.style = Style.BorderBlack
            lastSelectedButtonUser = Some(this)
          case Failure(exception) =>
            println(s"Error finding card index: ${exception.getMessage}")
        }
      }
      style = "-fx-background-color: transparent;"
    }
  }

  def createButton(label: String, action: => Unit): Button = {
    new Button {
      text = label
      minWidth = 80
      minHeight = 25
      onAction = _ =>
        action
      style = Style.buttonStyle
    }
  }


  def createKnockButton: Button = {
    new Button {
      text = "Knock"
      style <== when(disable) choose Style.disabledButton otherwise Style.buttonStyle
      disable <== firstRoundKnockValid
      onAction = _ => {
        controller.knock()
      }
    }
  }

  def createTradeOneButton: Button = {
    new Button {
      text = "Trade One"
      style <== when(disable) choose Style.disabledButton otherwise Style.buttonStyle
      disable <== cardIndexValid.not
      onAction = _ => {
        controller.tradeOne(cardIndex._1, cardIndex._2)
        cardIndex = (-1, -1)
        cardIndexValid.value = false
      }
    }
  }

  private def loadCardDeck(folderPath: String): Map[Card, String] = {
    val folderUrl: Option[URL] = Option(getClass.getClassLoader.getResource(folderPath))

    folderUrl match {
      case Some(url) =>
        val folder = new File(url.toURI) // Convert URL to File
        Try(folder.listFiles().filter(_.getName.endsWith(".png"))) match {
          case Success(cardFiles) =>
            cardFiles.flatMap { file =>
              val fileName = file.getName.replace(".png", "")
              val cardNameParts = fileName.split(" ")
              if (cardNameParts.length == 2) {
                val suit = cardNameParts(0)
                val rank = cardNameParts(1)
                Some(Card(suit, rank) -> file.getName) // Map Card to filename
              } else {
                println(s"Skipping invalid card file: ${file.getName}")
                None
              }
            }.toMap
          case Failure(exception) =>
            println(s"Error reading card files: ${exception.getMessage}")
            Map.empty
        }
      case None =>
        println(s"Folder not found: $folderPath")
        Map.empty
    }
  }
}