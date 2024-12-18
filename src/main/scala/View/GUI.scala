package View

import Controller.util.Controller
import _root_.Controller.HelpFunctions
import Model.Card
import _root_.Controller.util.Observer
import javafx.geometry.*
import scalafx.Includes.*
import scalafx.application.{JFXApp3, Platform}
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{HBox, StackPane, VBox}
import scalafx.scene.paint.Color.*

import scala.jdk.CollectionConverters.*
import java.net.URL
import java.io.File
import scala.util.{Failure, Success, Try}


class GUI(controller: Controller) extends JFXApp3 with Observer {

  controller.add(this)

  private val folderPath = "\\resources\\Karten\\"
  private val cardFileMap = loadCardDeck(folderPath)

  override def update(): Unit = {
    Platform.runLater {
      guiupdate()
    }
  }

  private def guiupdate(): Unit = {
    stage.scene = new Scene {
        fill = LightGreen
        content = new VBox {
          spacing = 20
          children = Seq(
            new Label {
              text = s"Knock Counter: ${controller.gameState.knockCounter}"
            },
            new Button {
              text = "Knock"
              onAction = _=> {
                controller.knock()
              }
            },
            new Label("Table's HandDeck:"),
            createCardDisplay(controller.gameState.table.handDeck),
          )
        }
      }
  }

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title.value = "Game of Schwimmen"
      width = Double.NaN
      height = Double.NaN
      scene = new Scene {
        fill = LightBlue
        // StackPane as root to center everything
        content = new StackPane {
          children = Seq(
            new VBox {
              alignment = Pos.CENTER
              spacing = 20
              children = Seq(
                // Table cards centered horizontally at the top
                new StackPane {
                  alignment = Pos.CENTER  // Center the cards horizontally
                  children = createCardDisplay(controller.gameState.table.handDeck)
                },

                // Add 3 additional cards below the table cards, centered horizontally
                new StackPane {
                  alignment = Pos.CENTER  // Center the additional cards horizontally
                  children = new HBox {
                    spacing = 10
                    alignment = Pos.CENTER // Center the cards within the HBox
                    children = createCardDisplay(HelpFunctions.getCurrentPlayer(controller.gameState).handDeck)
                  }
                },

                // Buttons centered horizontally at the bottom
                new StackPane {
                  alignment = Pos.CENTER  // Center the buttons horizontally
                  children = new HBox {
                    spacing = 20
                    alignment = Pos.CENTER // Center the buttons within the HBox
                    children = Seq(
                      new Button {
                        text = "Knock"
                        onAction = _=> {
                          controller.knock()
                        }
                      },
                      new Button {
                        text = "New Button"
                        onAction = _=> {
                          controller.skip()
                        }
                      }
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

  private def createCardImageView(card: Card): ImageView = {
    val cardImagePath = cardFileMap.getOrElse(card, "default.png")

    new ImageView {
      image = new Image(getClass.getClassLoader.getResourceAsStream(s"$folderPath$cardImagePath"))
      fitWidth = 100
      preserveRatio = true
    }
  }


  private def createCardDisplay(handDeck: Seq[Card]): HBox = {
    new HBox {
      spacing = 10
      children = handDeck.map { card =>
        createCardImageView(card)
      }
    }
  }

}
