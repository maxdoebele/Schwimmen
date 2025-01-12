package View.gui

import Model.BaseImpl.Card
import _root_.Controller.{Controller, HelpFunctions}
import com.google.inject.Inject
import javafx.geometry._
import scalafx.Includes._
import scalafx.application.{JFXApp3, Platform}
import scalafx.beans.binding.Bindings
import scalafx.beans.property.BooleanProperty
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{Border, BorderStroke, BorderStrokeStyle, BorderWidths, CornerRadii, GridPane, HBox, Region, StackPane, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.paint.Color._
import scalafx.scene.text.{Text, TextAlignment}
import util.Observer

import java.io.File
import java.net.URL
import java.util.concurrent.atomic.AtomicInteger
import scala.jdk.CollectionConverters._
import scala.util.{Failure, Success, Try}

class GUI @Inject() (val controller: Controller) extends JFXApp3 with Observer {

  controller.add(this)

  private val folderPath1 = "Karten/"
  private val Logo = "Logo.png"
  private val cardFileMap = loadCardDeck(folderPath1)

  private var cardIndex = (-1, -1)
  private var cardIndexValid = BooleanProperty(false)
  private var firstRoundKnockValid = BooleanProperty(false)
  private var lastSelectedButtonTable: Option[Button] = None
  private var lastSelectedButtonUser: Option[Button] = None

  private val roundCounter: AtomicInteger = new AtomicInteger(1)

  override def update(): Unit = {
    Platform.runLater {
      firstRoundKnockValid.value = controller.gameState.queue < controller.gameState.players.size
      if (controller.gameState.roundCounter == roundCounter.get()) {
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
    val playerNamesText = new Text {
      wrappingWidth = 250
      style = Style.defaultTextWhite
      text = "Spieler: "
    }
    new Scene(500, 300) {
      root = new StackPane {
        style = "-fx-background-color: lightblue;"
        alignment = Pos.CENTER
        children = new VBox {
          alignment = Pos.CENTER
          children = Seq(
            new Label {
              text = "Willkommen bei"
              style = Style.boldTextWhite
            },
            new ImageView (Logo) {
              fitWidth = 200
              preserveRatio = true
            },
            new VBox {
              alignment = Pos.CENTER
              spacing = 20
              children = Seq(
                new TextField {
                  text = "Gib einen Namen ein und drücke Enter"
                  style = Style.defaultTextWhite
                  maxWidth = 250
                  maxHeight = 30
                  onAction = _ => {
                    val name = text.value.trim
                    if (name.nonEmpty) {
                      enteredNames = enteredNames :+ name
                      playerNamesText.text = s"Spieler:\n ${enteredNames.mkString("\n")}"
                      text = ""
                    }
                  }
                },
                playerNamesText,
                new Button {
                  text = "Start"
                  minWidth = 100
                  style = Style.buttonStyle
                  onAction = _ => {
                    controller.createNewGame(enteredNames)
                  }
                }
              )
            }
          )
        }
      }
    }
  }


  private def guiupdatescene(): Scene = {
    new Scene(700, 500) {
      root = new StackPane {
        style = Style.Background
        alignment = Pos.CENTER
        children = new StackPane {
          alignment = Pos.CENTER
          children = Seq(
            new VBox {
              alignment = Pos.CENTER
              spacing = 20
              children = Seq(
                new Label {
                  text = "Table Cards"
                  style = Style.boldText
                },
                new HBox {
                  alignment = Pos.CENTER
                  children = createCardDisplayTable(controller.gameState.table.handDeck)
                },
                new Label {
                  text = s"${HelpFunctions.getCurrentPlayer(controller.gameState).name}'s Cards"
                  style = Style.boldText
                },
                new HBox {
                  alignment = Pos.CENTER
                  spacing = 10
                  alignment = Pos.CENTER
                  children = createCardDisplayUser(HelpFunctions.getCurrentPlayer(controller.gameState).handDeck)
                },
                new HBox {
                  alignment = Pos.CENTER
                  spacing = 20
                  alignment = Pos.CENTER
                  children = Seq(
                    createKnockButton,
                    createButton("Skip", controller.skip()),
                    createButton("Trade ALL", controller.tradeAll()),
                    createTradeOneButton
                  )
                }
              )
            }
          )
        }
      }
    }
  }

  def guiEndOfRoundScene(): Scene = {
    // val losers = controller.gameState.lastLoosers.map(_.name).mkString(", ")
    val currentScores = HelpFunctions.calculateCurrentScore(controller)
    val scoreGrid = new GridPane {
      hgap = 20
      vgap = 10
      alignment = Pos.CENTER
      padding = Insets(10)

      currentScores.zipWithIndex.foreach { case ((name, score), index) =>
        val nameLabel = new Label {
          text = name
          alignment = Pos.CENTER_LEFT
          style = Style.defaultText
        }
        val scoreLabel = new Label {
          text = score.toString
          alignment = Pos.CENTER_RIGHT
          style = Style.defaultText
        }
        add(nameLabel, 0, index)
        add(scoreLabel, 1, index)
      }
    }
    val currentScoresText = new Text {
      wrappingWidth = 250
      textAlignment = TextAlignment.Center
      style = Style.defaultText
      text = "Aktueller Punktestand"
    }
    new Scene(500, 300) {
      root = new StackPane {
        style = "-fx-background-color: lightblue;"
        alignment = Pos.CENTER
        children = new VBox {
          alignment = Pos.CENTER
          children = Seq(
            new Label {
              text = "Die Runde ist vorbei!"
              style = Style.boldText
            },
            currentScoresText,
            scoreGrid,
            new Region { minHeight = 20 },
            new Button {
              text = "Weiter"
              minWidth = 80
              minHeight = 25
              onAction = _ => {
                stage.scene = guiupdatescene()
              }
            }
          )
        }
      }
    }
  }


  private def createCardDisplayTable(handDeck: Seq[Card]): HBox = {
    new HBox {
      spacing = 10
      alignment = Pos.CENTER
      children = handDeck.map(createCardButtonTable)
    }
  }

  private def createCardButtonTable(card: Card): Button = {
    val cardImagePath = cardFileMap.getOrElse(card, "default.png")

    new Button {
      graphic = new ImageView {
        image = new Image(getClass.getClassLoader.getResourceAsStream(s"$folderPath1$cardImagePath"))
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

  private def createCardDisplayUser(handDeck: Seq[Card]): HBox = {
    new HBox {
      spacing = 10
      alignment = Pos.CENTER
      children = handDeck.map(createCardButtonUser)
    }
  }

  private def createCardButtonUser(card: Card): Button = {
    val cardImagePath = cardFileMap.getOrElse(card, "default.png")

    new Button {
      graphic = new ImageView {
        image = new Image(getClass.getClassLoader.getResourceAsStream(s"$folderPath1$cardImagePath"))
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

  private def createButton(label: String, action: => Unit): Button = {
    new Button {
      text = label
      minWidth = 80
      minHeight = 25
      onAction = _ =>
        action
      style = Style.buttonStyle
    }
  }

  private def createTradeOneButton: Button = {
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

  private def createKnockButton: Button = {
    new Button {
      text = "Knock"
      style <== when(disable) choose Style.disabledButton otherwise Style.buttonStyle
      disable <== firstRoundKnockValid
      onAction = _ => {
        controller.knock()
      }
    }
  }


  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title.value = "Game of Schwimmen"
      scene = guistartscene()
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