package View

import Controller.util.Controller
import Model.Card
import _root_.Controller.util.Observer
import scalafx.Includes.*
import scalafx.application.{JFXApp3, Platform}
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.paint.Color.*

import scala.jdk.CollectionConverters.*
import java.net.URL
import java.io.File
import scala.util.{Failure, Success, Try}


class GUI(controller: Controller) extends JFXApp3 with Observer {

  controller.add(this)

  private val folderPath = "\\resources\\Karten\\"
  val cardFileMap = loadCardDeck(folderPath)

  override def update(): Unit = {
    Platform.runLater {
      guiupdate()
    }
  }

  def guiupdate(): Unit = {
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
              onAction = handle {
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
      title.value = "Hello Stage"
      width = 600
      height = 450
      scene = new Scene {
        fill = LightGreen
        content = new VBox {
          spacing = 20
          children = Seq(
            new Label {
              text = s"Knock Counter: ${controller.gameState.knockCounter}"
            },
            new Button {
              text = "Knock"
              onAction = handle {
                controller.knock()
              }
            },
            new Label("Table's HandDeck:"),
            createCardDisplay(controller.gameState.table.handDeck),
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
    val cardImagePath = cardFileMap.get(card).getOrElse("default.png")

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
