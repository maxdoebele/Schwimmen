package View.gui

import Model.BaseImpl.Card
import _root_.Controller.{Controller, HelpFunctions}
import javafx.geometry._
import scalafx.Includes._
import scalafx.application.{JFXApp3, Platform}
import scalafx.beans.property.BooleanProperty
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{HBox, Region, StackPane, VBox}
import scalafx.scene.paint.Color._
import scalafx.scene.text.Text
import util.Observer

import java.io.File
import java.net.URL
import java.util.concurrent.atomic.AtomicInteger
import scala.jdk.CollectionConverters._
import scala.util.{Failure, Success, Try}

class GUI(controller: Controller) extends JFXApp3 with Observer {

  controller.add(this)

  private val folderPath = "Karten/"
  private val cardFileMap = loadCardDeck(folderPath)

  private var cardIndex = (-1, -1)
  private var cardIndexValid = BooleanProperty(false)
  private var lastSelectedButtonTable: Option[Button] = None
  private var lastSelectedButtonUser: Option[Button] = None

  private val roundCounter: AtomicInteger = new AtomicInteger(1)

  // end of round aus tui übernommen
  override def update(): Unit = {
    Platform.runLater {
      if (controller.gameState.roundCounter == roundCounter.get()) {
        stage.scene = guiEndOfRoundScene()
        this.synchronized {
          roundCounter.incrementAndGet()
        }
      } else if (controller.getPlayerNames.nonEmpty && stage.scene != guiupdatescene()) {
        stage.scene = guiupdatescene()
      }
    }
  }

  private def guistartscene(): Scene = {
    var enteredNames: Seq[String] = Seq.empty
    val playerNamesText = new Text {
      wrappingWidth = 250
      style = "-fx-font-size: 14px; -fx-fill: white;"
      text = "Spieler: "
    }
    if (controller.getPlayerNames.nonEmpty) { // guckt ob in tui schon was eingegeben wurde
      guiupdatescene()
    } else {
      new Scene(500, 300) {
        root = new StackPane {
          style = "-fx-background-color: darkblue;"
          alignment = Pos.CENTER
          children = new VBox {
            alignment = Pos.CENTER
            children = Seq(
              new Label {
                text = "Willkommen bei Schwimmen"
                style = "-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: white;"
              },
              new VBox {
                alignment = Pos.CENTER
                spacing = 20
                children = Seq(
                  new TextField {
                    text = "Gib einen Namen ein und drücke Enter"
                    style = "-fx-font-size: 14px; -fx-text-fill: black;"
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
                      if (enteredNames.nonEmpty) {
                        controller.setPlayerNames(enteredNames)
                        stage.scene = guiupdatescene()
                      } else {
                        println("Keine Spielernamen eingegeben!")
                      }
                    }
                  }
                )
              }
            )
          }
        }
      }
    }
  }


  private def guiupdatescene(): Scene = {
    new Scene(700, 500) {
      root = new StackPane {
        style = "-fx-background-color: lightblue;"
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
                  style = "-fx-font-size: 16px; -fx-font-weight: bold;" // Styling for table cards header
                },
                new HBox {
                  alignment = Pos.CENTER
                  children = createCardDisplayTable(controller.gameState.table.handDeck)
                },
                new Label {
                  text = s"${HelpFunctions.getCurrentPlayer(controller.gameState).name}'s Cards"
                  style = "-fx-font-size: 16px; -fx-font-weight: bold;" // Styling for player cards header
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
                  children = Seq(
                    createButton("Knock", controller.knock()),
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
    val losers = controller.gameState.lastLoosers.map(_.name).mkString(", ")
    val spacer = new Region {
      minHeight = 20
    }
    new Scene(700, 500) {
      root = new StackPane {
        style = "-fx-background-color: lightblue;"
        alignment = Pos.CENTER
        children = new VBox {
          alignment = Pos.CENTER
          children = Seq(
            new Label {
              text = "Die Runde ist vorbei!"
              style = "-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: black;"
            },
            spacer,
            new Text {
              wrappingWidth = 250
              style = "-fx-font-size: 14px; -fx-fill: black;"
              text = s"Verloren hat: ${losers}"
            },
            spacer,
            new Text {
              wrappingWidth = 250
              text = "Aktueller Punktestand:"
              style = "-fx-font-size: 14px; -fx-text-fill: black;"
            },
            new VBox {
              spacing = 2
              alignment = Pos.CENTER
              children = HelpFunctions.calculateCurrentScore(controller).map { case (name, score) =>
                new Text {
                  text = f"$name%-20s  $score%3d"
                  style = "-fx-font-size: 14px; -fx-fill: black;"
                }
              }
            },
            spacer,
            new Label {
              text = "Bestätige um weiter zu spielen:"
              style = "-fx-font-size: 14px; -fx-text-fill: black;"
            },
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
            this.style = "-fx-background-color: transparent; -fx-border-color: red; -fx-border-width: 2px;"
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
            this.style = "-fx-background-color: transparent; -fx-border-color: red; -fx-border-width: 2px;"
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
      onAction = _ => action
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
