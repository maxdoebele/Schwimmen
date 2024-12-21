package View.gui

import Model.BaseImpl.Card
import _root_.Controller.{Controller, HelpFunctions}
import javafx.geometry.*
import scalafx.Includes.*
import scalafx.application.{JFXApp3, Platform}
import scalafx.beans.property.BooleanProperty
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{HBox, StackPane, VBox}
import scalafx.scene.paint.Color.*
import util.Observer

import java.io.File
import java.net.URL
import scala.jdk.CollectionConverters.*
import scala.util.{Failure, Success, Try}

class GUI(controller: Controller) extends JFXApp3 with Observer {

  controller.add(this)

  private val folderPath = "Karten/"
  private val cardFileMap = loadCardDeck(folderPath)

  private var cardIndex = (-1, -1)
  private var cardIndexValid = BooleanProperty(false)
  private var lastSelectedButtonTable: Option[Button] = None
  private var lastSelectedButtonUser: Option[Button] = None

  override def update(): Unit = {
    Platform.runLater {
      guiupdate()
    }
  }

  private def guiupdate(): Unit = {
    stage.scene = new Scene {
      fill = LightBlue
      content = new StackPane {
        children = Seq(
          new VBox {
            alignment = Pos.CENTER
            spacing = 20
            children = Seq(
              // Header for Table Cards
              new Label {
                text = "Table Cards"
                style = "-fx-font-size: 16px; -fx-font-weight: bold;" // Styling for table cards header
              },

              new StackPane {
                alignment = Pos.CENTER
                children = createCardDisplayTable(controller.gameState.table.handDeck)
              },

              new Label {
                text = s"${HelpFunctions.getCurrentPlayer(controller.gameState).name}'s Cards"
                style = "-fx-font-size: 16px; -fx-font-weight: bold;" // Styling for player cards header
              },

              new StackPane {
                alignment = Pos.CENTER
                children = new HBox {
                  spacing = 10
                  alignment = Pos.CENTER
                  children = createCardDisplayUser(HelpFunctions.getCurrentPlayer(controller.gameState).handDeck)
                }
              },

              new StackPane {
                alignment = Pos.CENTER
                children = new HBox {
                  spacing = 20
                  alignment = Pos.CENTER
                  children = Seq(
                    createButton("Knock", controller.knock()),
                    createButton("Skip", controller.skip()),
                    createButton("Trade ALL", controller.tradeAll()),
                    createTradeOneButton
                  )
                }
              }
            )
          }
        )
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
      scene = new Scene {
        fill = LightBlue
        content = new StackPane {
          children = Seq(
            new VBox {
              alignment = Pos.CENTER
              spacing = 20
              children = Seq(
                new Label {
                  text = "Table Cards"
                  style = "-fx-font-size: 16px; -fx-font-weight: bold;"
                },
                new StackPane {
                  alignment = Pos.CENTER
                  children = createCardDisplayTable(controller.gameState.table.handDeck)
                },
                new Label {
                  text = s"${HelpFunctions.getCurrentPlayer(controller.gameState).name}'s Cards"
                  style = "-fx-font-size: 16px; -fx-font-weight: bold;"
                },
                new StackPane {
                  alignment = Pos.CENTER
                  children = new HBox {
                    spacing = 10
                    alignment = Pos.CENTER
                    children = createCardDisplayUser(HelpFunctions.getCurrentPlayer(controller.gameState).handDeck)
                  }
                },

                new StackPane {
                  alignment = Pos.CENTER
                  children = new HBox {
                    spacing = 20
                    alignment = Pos.CENTER
                    children = Seq(
                      createButton("Knock", controller.knock()),
                      createButton("Skip", controller.skip()),
                      createButton("Trade ALL", controller.tradeAll()),
                      createTradeOneButton
                    )
                  }
                }
              )
            }
          )
        }
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
